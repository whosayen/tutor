package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Booking;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.BookingType;
import com.lectorie.lectorie.enums.Status;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record BookingDto (
        String id,

        BookingType type,

        Status status,

        ZonedDateTime time,

        BookingEnrollmentDto enrollmentDto,

        BookingDuration bookingDuration,

        Double price
) {

    public static BookingDto convert(Booking from, ZoneId zoneId) {

        return new BookingDto(
                from.getId(),
                getBookingType(from, zoneId),
                from.getStatus(),
                from.getTime().withZoneSameInstant(zoneId),
                BookingEnrollmentDto.convert(from.getEnrollment()),
                from.getBookingDuration(),
                from.getPrice()
        );
    }



    private static BookingType getBookingType(Booking booking, ZoneId zoneId) {
        ZonedDateTime zonedStartTime = booking.getTime().withZoneSameInstant(zoneId);
        ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        Duration duration = Duration.between(currentTime, zonedStartTime);

        if (booking.getStatus().equals(Status.DECLINED)) {
            return BookingType.DECLINED;
        } else if (booking.getStatus() == Status.CONFIRMED && !duration.isNegative() && duration.toHours() < 24) {
            return BookingType.UPCOMING;
        } else if (booking.getStatus() == Status.CONFIRMED) {
            return BookingType.FUTURE;
        } else if (duration.isNegative()) {
            return BookingType.PAST;
        }
        return BookingType.NOT_ACCEPTED_YET;

    }
}
