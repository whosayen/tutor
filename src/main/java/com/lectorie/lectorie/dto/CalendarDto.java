package com.lectorie.lectorie.dto;


import java.util.List;

public record CalendarDto (
        List<BookingStartTimeDto> bookingStartTimeDtoList
) {
}
