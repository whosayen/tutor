package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Booking;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.Status;

import java.time.ZonedDateTime;

public record EnrollmentBookingDto(
        String id,
        Status status,
        ZonedDateTime time,
        BookingDuration bookingDuration,
        Double price
) {
    public static EnrollmentBookingDto convert(Booking from) {
        return new EnrollmentBookingDto(
                from.getId(),
                from.getStatus(),
                from.getTime(),
                from.getBookingDuration(),
                from.getPrice()
        );
    }
}
