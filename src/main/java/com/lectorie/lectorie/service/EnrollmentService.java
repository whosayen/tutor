package com.lectorie.lectorie.service;


import com.lectorie.lectorie.dto.BookingDto;
import com.lectorie.lectorie.dto.EnrollmentDto;
import com.lectorie.lectorie.dto.request.BookingRequest;
import com.lectorie.lectorie.dto.request.CancelBookingRequest;
import com.lectorie.lectorie.dto.request.RescheduleBookingRequest;
import com.lectorie.lectorie.dto.response.PaymentResponse;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.Status;
import com.lectorie.lectorie.exception.custom.*;
import com.lectorie.lectorie.model.*;
import com.lectorie.lectorie.repository.BookingRepository;
import com.lectorie.lectorie.repository.EnrollmentRepository;
import com.lectorie.lectorie.repository.TutorRepository;
import com.lectorie.lectorie.repository.UserRepository;
import com.lectorie.lectorie.util.UserUtil;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
public class EnrollmentService {

    @Value("${frontend.url}")
    private String frontendUrl;


    private final EnrollmentRepository repository;
    private final UserUtil userUtil;
    private final TutorService tutorService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;


    public EnrollmentService(EnrollmentRepository repository, UserUtil userUtil, TutorService tutorService, BookingService bookingService, PaymentService paymentService, BookingRepository bookingRepository, UserRepository userRepository, TutorRepository tutorRepository) {
        this.repository = repository;
        this.userUtil = userUtil;
        this.tutorService = tutorService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
    }

    protected List<Booking> findBookingsWithinPeriod(Enrollment enrollment, ZonedDateTime startTime, ZonedDateTime finishTime) {
        return repository.findBookingsWithinPeriod(enrollment, startTime, finishTime);

    }

    public void createEnrollmentAndBooking(String userId, String tutorId, ZonedDateTime startTime, BookingDuration  bookingDuration, double price) throws StripeException {
        // Retrieve user settings and tutor
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("there is no user", 3990));
        UserSettings us = user.getUserSettings();

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new TutorNotFoundException("there is no tutor", 3990));

        userRepository.save(
                user.changeBalance(Math.max(user.getBalance() - price, 0))
        );

        Enrollment enrollment = repository.findEnrollmentsByUserSettingsAndTutor(us, tutor)
                .orElseGet(() -> repository.save(new Enrollment(tutor, us)));

        bookingService.createBooking(startTime, enrollment, bookingDuration, price);
    }

    public PaymentResponse book(Principal currentUser, BookingRequest bookingRequest) throws StripeException {
        Tutor tutor = tutorService.getTutor(bookingRequest.tutorId());
        User user = userUtil.extractUser(currentUser);

        ZoneId tutorZoneId = tutor.getUser().getUserSettings().getTimezone();

        ZonedDateTime startTime = bookingRequest.startTime().withZoneSameInstant(tutorZoneId);
        BookingDuration bookingDuration = bookingRequest.bookingDuration();
        ZonedDateTime endTime = startTime.plusMinutes(bookingDuration.getDurationMinutes());

        List<Enrollment> userEnrollments = user.getUserSettings().getEnrollments();
        boolean isUserAvailable = checkAvailability(userEnrollments, startTime, endTime);
        if (!isUserAvailable) {
            throw new BookingException("User has a booking conflict.", 3997);
        }

        List<Enrollment> tutorEnrollments = tutor.getEnrollments();
        boolean isTutorAvailable = checkAvailability(tutorEnrollments, startTime, endTime);
        if (!isTutorAvailable) {
            throw new BookingException("Tutor has a booking conflict.", 3997);
        }

        boolean isWithinSchedule = true; //checkTutorSchedule(tutor.getSchedule(), startTime.toLocalTime(), endTime.toLocalTime(), startTime.getDayOfWeek(), endTime.getDayOfWeek());
        if (!isWithinSchedule) {
            throw new BookingException("Tutor is not available at the requested time.", 3997);
        }

        double price = tutor.getHourlyRate() * bookingRequest.bookingDuration().getDurationMinutes() / 60 * 100;

        double newPrice = Math.max(price - user.getBalance(), 0);

        if (newPrice != 0) {
            return paymentService.createPaymentLink(
                    user.getId(),
                    tutor.getId(),
                    bookingRequest.startTime(),
                    tutor.getLanguageToTeach().getName(),
                    newPrice,
                    bookingRequest.bookingDuration(),
                    tutor.getHourlyRate()
            );
        }

        createEnrollmentAndBooking(user.getId(), tutor.getId(), bookingRequest.startTime(), bookingRequest.bookingDuration(), price);

        return new PaymentResponse(frontendUrl + "/success", "no_payment");
    }

    private boolean checkTutorSchedule(Schedule schedule, LocalTime startTime, LocalTime endTime, DayOfWeek startDay, DayOfWeek endDay) {
        // Check availability for the day the booking starts
        List<TimeInterval> availableTimesStartDay = schedule.getAvailableDay(startDay);
        boolean isAvailableStartDay = checkTimeIntervals(availableTimesStartDay, startTime, LocalTime.MAX); // check from startTime to the end of the day

        if (!isAvailableStartDay) {
            return false; // No availability on the start day
        }

        // If the booking ends on the same day, we only need to check that one day
        if (startDay.equals(endDay)) {
            return checkTimeIntervals(availableTimesStartDay, startTime, endTime);
        }

        // If the booking spans into the next day, check availability for the end day as well
        List<TimeInterval> availableTimesEndDay = schedule.getAvailableDay(endDay);

        return checkTimeIntervals(availableTimesEndDay, LocalTime.MIN, endTime);
    }

    // Helper method to check if a time range fits within available time intervals
    private boolean checkTimeIntervals(List<TimeInterval> availableTimes, LocalTime startTime, LocalTime endTime) {
        for (TimeInterval interval : availableTimes) {
            if (!startTime.isBefore(interval.getStartTime()) && !endTime.isAfter(interval.getEndTime())) {
                return true; // Time is within an available interval
            }
        }
        return false; // No available interval found
    }
    private boolean checkAvailability(List<Enrollment> enrollments, ZonedDateTime startTime, ZonedDateTime endTime) {
        for (Enrollment enrollment : enrollments) {
            for (Booking booking : enrollment.getBookings()) {
                ZonedDateTime bookingStart = booking.getTime();
                ZonedDateTime bookingEnd = bookingStart.plusMinutes(booking.getBookingDuration().getDurationMinutes());

                // Corrected overlap check
                if (startTime.isBefore(bookingEnd) && endTime.isAfter(bookingStart)) {
                    return false; // Conflict found
                }
            }
        }
        return true; // No conflict found
    }

    public String cancelBooking(Principal currentUser, String id) {
        User user = userUtil.extractUser(currentUser);
        UserSettings us = user.getUserSettings();
        Tutor tutor = user.getTutor();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("There is no booking with this id.",3990));

        //afterwards delete booking. if enrollments only booking is this booking delete enrollment as well
        Enrollment enrollment = booking.getEnrollment();
        if (!enrollment.getUserSettings().getId().equals(us.getId()) && (tutor == null || !enrollment.getTutor().getId().equals(tutor.getId()))) {
            throw new LessonNotFoundException("You are not authorized to cancel this booking.", 3991);
        }

        enrollment.getBookings().remove(booking);
        // Remove the booking
        bookingRepository.delete(booking);
        // Check if the enrollment has no other bookings, and if so, delete the enrollment
        if (enrollment.getBookings().isEmpty()) {
            repository.delete(enrollment);
        } else {
            repository.save(enrollment);
        }



        return "Has been deleted successfully.";
    }

    public String rescheduleBooking(Principal currentUser, RescheduleBookingRequest rescheduleBookingRequest) {
        User user = userUtil.extractUser(currentUser);

        if (rescheduleBookingRequest.newTime().isBefore(rescheduleBookingRequest.previousTime())) {
            throw new RescheduleTimeError("You can't go back in time you fool.", 3990);
        }

        if (Duration.between(rescheduleBookingRequest.previousTime(), rescheduleBookingRequest.newTime()).toHours() > 24) {
            throw new RescheduleTimeError("You cant reschedule less than 24 hours.", 3990);
        }

        Enrollment enrollment = repository.findEnrollmentsByUserSettingsAndTutor(
                user.getUserSettings(),
                tutorService.getTutor(rescheduleBookingRequest.tutorId())
        ).orElseThrow(() -> new LessonNotFoundException("You don't have lesson to reschedule.",3990));

        bookingService.rescheduleBooking(enrollment, rescheduleBookingRequest);

        return "Lesson reschedule request.";
    }

    public BookingDto finishBooking(Principal currentUser, String bookId, Boolean isApproved) {
        // TODO FINISH BOOKING VALIDASYON EKLE. ONAYLANAN BIRDAHA ONAYLANIYOR

        User user = userUtil.extractUser(currentUser);

        Booking booking = bookingRepository.findById(bookId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found.",3990));

        if (!booking.getEnrollment().getUserSettings().getId().equals(user.getUserSettings().getId())) {
            throw new BookingException("You are not the owner of this booking.",3990);
        }

        User tutorUser = booking.getEnrollment().getTutor().getUser();
        if (isApproved) {
            userRepository.save(
                    tutorUser.changeBalance(tutorUser.getBalance() + booking.getPrice())
            );
        } else {
            userRepository.save(
                    user.changeBalance(user.getBalance() + booking.getPrice())
            );
        }

        return BookingDto.convert(bookingRepository.save(
                booking.changeStatus((isApproved) ? Status.DONE : Status.NOT_DONE)
        ), user.getUserSettings().getTimezone());
    }

    public EnrollmentDto rateAndComment(Principal currentUser, String tutorId, Integer rate, String comment) throws DataFormatException, IOException {
        User user = userUtil.extractUser(currentUser);
        UserSettings userSettings = user.getUserSettings();

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new TutorNotFoundException("Tutor not found.",3990));

        Enrollment enrollment = repository.findEnrollmentsByUserSettingsAndTutor(userSettings, tutor)
                .orElseThrow(() -> new RuntimeException("Enrollment not found."));

        boolean hasCompletedBooking = enrollment.getBookings().stream()
                .anyMatch(booking -> booking.getStatus() == Status.DONE);

        if (!hasCompletedBooking) {
            throw new RuntimeException("At least one booking must be completed (DONE) to rate and comment.");
        }

        if (rate > 5 || rate < 0) {
            throw new RuntimeException("Rate must be between 0 and 5");
        }

        Enrollment newEnrollment = enrollment
                        .changeRate(rate)
                        .changeComment(comment);

        return EnrollmentDto.convert(repository.save(newEnrollment));
    }

    public List<EnrollmentDto> getTutorEnrollments(String tutorId) {
        return repository.findEnrollmentsByTutorId(tutorId)
                .stream()
                .map(enrollment -> {
                    try {
                        return EnrollmentDto.convert(enrollment);
                    } catch (DataFormatException | IOException e) {
                        // Handle the exception as needed
                        // For example, you can rethrow it as a runtime exception
                        throw new RuntimeException("Error converting Enrollment to EnrollmentDto", e);
                    }
                })

                .toList();
    }
}
