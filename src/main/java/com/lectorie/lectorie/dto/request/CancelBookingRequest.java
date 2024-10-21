package com.lectorie.lectorie.dto.request;


import java.time.ZonedDateTime;

public record CancelBookingRequest(
        ZonedDateTime startTime,
        String tutorId
) {
}
