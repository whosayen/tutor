package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.BookingDto;
import com.lectorie.lectorie.dto.request.RescheduleBookingRequest;
import com.lectorie.lectorie.dto.response.PaymentResponse;
import com.lectorie.lectorie.exception.custom.BookingException;
import com.lectorie.lectorie.model.Booking;
import com.lectorie.lectorie.model.Enrollment;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.Status;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.repository.BookingRepository;
import com.lectorie.lectorie.repository.UserRepository;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository repository;
    private final UserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;


    public BookingService(BookingRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Booking createBooking(ZonedDateTime startTime, Enrollment enrollment, BookingDuration bookingDuration, double price) throws StripeException {
        return repository.save(
                new Booking(
                        startTime,
                        enrollment,
                        bookingDuration,
                        price
                )
        );
    }

    // return true if enrollment is empty
    //todo check might be wrong
    protected boolean deleteBookingByEnrollmentAndStartTime(Enrollment enrollment, ZonedDateTime zonedDateTime) {
        User user = enrollment.getUserSettings().getUser();

        enrollment.getBookings().forEach(booking -> {
            if (booking.getTime().isEqual(zonedDateTime)) {
                repository.delete(booking);

                userRepository.save(
                        user.changeBalance(user.getBalance() + booking.getPrice())
                );
            }
        });

        return enrollment.getBookings().isEmpty();
    }



    public void rescheduleBooking(Enrollment enrollment, RescheduleBookingRequest rescheduleBookingRequest) {
        enrollment.getBookings().forEach(booking -> {
            if (booking.getTime().isEqual(rescheduleBookingRequest.previousTime())) {

                if (booking.getStatus() == Status.DONE) {
                    throw new BookingException("Booking already ended",3990);
                }

                repository.save(
                        booking
                                .changeStatus(Status.WAITING)
                                .changeTime(rescheduleBookingRequest.newTime())
                );
            }
        });
    }


    @Scheduled(fixedRate = 60000) // Her 1 dakikada bir çalışacak
    public void processWaitingBookings() {
        System.out.println("Processing waiting bookings");
        ZonedDateTime now = ZonedDateTime.now();
        List<Booking> waitingBookings = repository.findByStatusAndTimeBefore(Status.WAITING, now);

        for (Booking booking : waitingBookings) {
            handleExpiredBooking(booking);
        }
    }

    private void handleExpiredBooking(Booking booking) {
        repository.save(
                booking.changeStatus(Status.DECLINED)
        );

        User user = booking.getEnrollment().getUserSettings().getUser();
        userRepository.save(
                user.changeBalance(user.getBalance() + booking.getPrice())
        );
    }
}
