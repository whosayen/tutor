package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.TimeInterval;

import java.time.LocalTime;

public record TimeIntervalDto(
        LocalTime start,
        LocalTime end
) {

    public static TimeIntervalDto convert(TimeInterval from) {
        return new TimeIntervalDto(
                from.getStartTime(),
                from.getEndTime()
        );
    }
}
