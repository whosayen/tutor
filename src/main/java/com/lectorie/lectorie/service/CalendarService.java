package com.lectorie.lectorie.service;


import com.lectorie.lectorie.dto.BookingStartTimeDto;
import com.lectorie.lectorie.dto.CalendarDto;
import com.lectorie.lectorie.model.*;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.TimeIntervalType;
import com.lectorie.lectorie.util.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.security.Principal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CalendarService {

    private final UserUtil userUtil;
    private final TutorService tutorService;
    private final EnrollmentService enrollmentService;

    @Value("${availability.split-time}")
    private int availableSplitTime;

    public CalendarService(UserUtil userUtil, TutorService tutorService, EnrollmentService enrollmentService) {
        this.userUtil = userUtil;
        this.tutorService = tutorService;
        this.enrollmentService = enrollmentService;
    }


    public CalendarDto getCalender(Principal currentUser, int weekLater, String userId, BookingDuration bookingDuration) {

        UserSettings us;
        if (currentUser != null) {
            User user = userUtil.extractUser(currentUser);
            us = user.getUserSettings();
        } else {
            us = null;
        }

        Tutor tutor = tutorService.getTutor(userId);
        UserSettings tutorUs = tutor.getUser().getUserSettings();

        Schedule schedule = tutor.getSchedule();

        CalendarDto calendarDto = getCalendarDtoFromSchedule(schedule, weekLater, (us == null) ? ZoneId.of("UTC") : us.getTimezone(), tutorUs.getTimezone(), bookingDuration);

        return updateCalenderDtoFromBookings(calendarDto, getTutorBookings(tutor, weekLater), getStudentBookings(us, weekLater), (us == null) ? null : us.getId());
    }

    private List<Booking> getTutorBookings(Tutor tutor, int weekLater) {
        List<Booking> tutorBooking =  new ArrayList<>();
        ZonedDateTime today = ZonedDateTime.now();

        for (Enrollment enrollment: tutor.getEnrollments()) {
            tutorBooking.addAll(
                    enrollmentService.findBookingsWithinPeriod(enrollment, today.plusWeeks(weekLater), today.plusWeeks(1 + weekLater))
            );
        }

        return tutorBooking;
    }

    private List<Booking> getStudentBookings(UserSettings userSettings, int weekLater) {
        List<Booking> studentBookings =  new ArrayList<>();
        if (userSettings == null) {
            return studentBookings;
        }
        ZonedDateTime today = ZonedDateTime.now();

        for (Enrollment enrollment: userSettings.getEnrollments()) {
            studentBookings.addAll(
                    enrollmentService.findBookingsWithinPeriod(enrollment, today.plusWeeks(weekLater), today.plusWeeks(1 + weekLater))
            );
        }
        return studentBookings;
    }


    private CalendarDto getCalendarDtoFromSchedule(Schedule schedule, int weekLater, ZoneId userZoneId, ZoneId tutorZoneId, BookingDuration bookingDuration) {
        ZonedDateTime nowInTutorZone = (weekLater == 0) ? ZonedDateTime.now(tutorZoneId) : ZonedDateTime.now(tutorZoneId).plusWeeks(weekLater).with(LocalTime.MIN);
        LocalDate tutorStartDate = nowInTutorZone.toLocalDate();
        List<BookingStartTimeDto> userAvailableTimes = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate currentDate = tutorStartDate.plusDays(day.getValue() - tutorStartDate.getDayOfWeek().getValue());
            List<TimeInterval> dailyIntervals = schedule.getAvailableDay(day);

            for (TimeInterval interval : dailyIntervals) {
                LocalTime startTime = interval.getStartTime();
                while (!startTime.isAfter(interval.getEndTime().minusMinutes(bookingDuration.getDurationMinutes()))) {
                    ZonedDateTime tutorTime = ZonedDateTime.of(currentDate, startTime, tutorZoneId);
                    if (tutorTime.isBefore(nowInTutorZone)) {
                        tutorTime = tutorTime.plusWeeks(1);
                    }
                    userAvailableTimes.add(new BookingStartTimeDto(tutorTime.withZoneSameInstant(userZoneId), TimeIntervalType.AVAILABLE));
                    startTime = startTime.plusMinutes(availableSplitTime); // Assuming 30 minutes increment
                }
            }
        }

        return new CalendarDto(userAvailableTimes);
    }

    private CalendarDto updateCalenderDtoFromBookings(CalendarDto calendarDto, List<Booking> tutorBookings, List<Booking> studentBookings, String userSettingsId) {

        List<BookingStartTimeDto> bookingStartTimeDtoList = calendarDto.bookingStartTimeDtoList();

        for (Booking booking : tutorBookings) {
            TimeIntervalType newType = TimeIntervalType.BOOKED;
            if (Objects.equals(booking.getEnrollment().getUserSettings().getId(), userSettingsId))
                newType = TimeIntervalType.BOOKED_BY_YOU;
            checkTimeIntervals(booking,newType,bookingStartTimeDtoList);
        }
        for (Booking booking : studentBookings) {
            checkTimeIntervals(booking,TimeIntervalType.YOU_ARE_NOT_AVAILABLE,bookingStartTimeDtoList);
        }
        return calendarDto;
    }

    private void checkTimeIntervals(Booking booking, TimeIntervalType timeIntervalType, List<BookingStartTimeDto> bookingStartTimeDtoList ) {
        if (booking.getBookingDuration() == BookingDuration.ONE_HOUR || booking.getBookingDuration() == BookingDuration.FORTY_FIVE_MINUTES) {
            changeAvailableBookingType(booking.getTime(), bookingStartTimeDtoList, timeIntervalType);
            changeAvailableBookingType(booking.getTime().plusMinutes(30), bookingStartTimeDtoList, timeIntervalType);
        }  else if (booking.getBookingDuration() == BookingDuration.THIRTY_MINUTES) {
            changeAvailableBookingType(booking.getTime(), bookingStartTimeDtoList, timeIntervalType);
        } else if (booking.getBookingDuration() == BookingDuration.ONE_AND_HALF_HOUR) {
            changeAvailableBookingType(booking.getTime(), bookingStartTimeDtoList, timeIntervalType);
            changeAvailableBookingType(booking.getTime().plusMinutes(30), bookingStartTimeDtoList, timeIntervalType);
            changeAvailableBookingType(booking.getTime().plusMinutes(60), bookingStartTimeDtoList, timeIntervalType);
        }
    }

    private void changeAvailableBookingType(ZonedDateTime bookingTime,
                                            List<BookingStartTimeDto> bookingStartTimeDtoList,
                                            TimeIntervalType newType) {
        for (int i = 0; i < bookingStartTimeDtoList.size(); i++) {
            BookingStartTimeDto dto = bookingStartTimeDtoList.get(i);

            if (dto.startTime().isEqual(bookingTime) && dto.type() == TimeIntervalType.AVAILABLE) {
                BookingStartTimeDto updatedDto = new BookingStartTimeDto(dto.startTime(), newType);
                bookingStartTimeDtoList.set(i, updatedDto);
                break;
            }
        }
    }

    public List<BookingDuration> getBookingDurations(String tutorId) {
        return tutorService.getBookingDurations(tutorId);
    }
}
