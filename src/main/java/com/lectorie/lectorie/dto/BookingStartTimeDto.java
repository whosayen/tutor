package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.enums.TimeIntervalType;

import java.time.ZonedDateTime;

public record BookingStartTimeDto (

        ZonedDateTime startTime,

        TimeIntervalType type

) {
}
