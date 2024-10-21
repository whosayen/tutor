package com.lectorie.lectorie.dto.request;


import java.time.ZonedDateTime;

public record RescheduleBookingRequest(
        ZonedDateTime previousTime,
        ZonedDateTime newTime,
        String tutorId
) {
}
