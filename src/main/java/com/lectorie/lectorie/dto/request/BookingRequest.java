package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.enums.BookingDuration;

import java.time.ZonedDateTime;

public record BookingRequest(
        String tutorId,

        ZonedDateTime startTime,

        BookingDuration bookingDuration
) {
}