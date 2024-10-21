package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Schedule;

import java.util.List;
import java.util.stream.Collectors;

public record ScheduleDto(

    List<TimeIntervalDto> monday,
    List<TimeIntervalDto> tuesday,
    List<TimeIntervalDto> wednesday,
    List<TimeIntervalDto> thursday,
    List<TimeIntervalDto> friday,
    List<TimeIntervalDto> saturday,
    List<TimeIntervalDto> sunday
) {

    public static ScheduleDto convert(Schedule from) {
        return new ScheduleDto(
                from.getMonday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList()),
                from.getTuesday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList()),
                from.getWednesday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList()),
                from.getThursday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList()),
                from.getFriday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList()),
                from.getSaturday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList()),
                from.getSunday().stream().map(TimeIntervalDto::convert).collect(Collectors.toList())
        );
    }

}
