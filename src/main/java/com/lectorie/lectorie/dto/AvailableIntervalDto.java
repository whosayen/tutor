package com.lectorie.lectorie.dto;

import java.time.LocalTime;

public record AvailableIntervalDto(
        LocalTime start,
        LocalTime end
) {

}
