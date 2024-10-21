package com.lectorie.lectorie.dto.request;


import com.lectorie.lectorie.dto.AvailableIntervalDto;

import java.util.List;

public record ScheduleSettingsRequest(
        List<AvailableIntervalDto> mondayAvailableTimes,

        List<AvailableIntervalDto> tuesdayAvailableTimes,

        List<AvailableIntervalDto> wednesdayAvailableTimes,

        List<AvailableIntervalDto> thursdayAvailableTimes,

        List<AvailableIntervalDto> fridayAvailableTimes,

        List<AvailableIntervalDto> saturdayAvailableTimes,

        List<AvailableIntervalDto> sundayAvailableTimes
) {
}
