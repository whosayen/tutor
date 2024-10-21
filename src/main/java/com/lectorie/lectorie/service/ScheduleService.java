package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.AvailableIntervalDto;
import com.lectorie.lectorie.dto.request.ScheduleSettingsRequest;
import com.lectorie.lectorie.exception.custom.ScheduleNotFoundException;
import com.lectorie.lectorie.model.Schedule;
import com.lectorie.lectorie.model.TimeInterval;
import com.lectorie.lectorie.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Value("${availability.split-time}")
    private int splitTime;

    private final ScheduleRepository scheduleRepository;
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public void setTutorScheduleWithIntervals(Long tutorScheduleId, ScheduleSettingsRequest request) {
        Schedule schedule = scheduleRepository.findById(tutorScheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found",3990));

        Schedule newSchedule = new Schedule(
                schedule.getId(),
                convertDtoToIntervals(request.mondayAvailableTimes()),
                convertDtoToIntervals(request.tuesdayAvailableTimes()),
                convertDtoToIntervals(request.wednesdayAvailableTimes()),
                convertDtoToIntervals(request.thursdayAvailableTimes()),
                convertDtoToIntervals(request.fridayAvailableTimes()),
                convertDtoToIntervals(request.saturdayAvailableTimes()),
                convertDtoToIntervals(request.sundayAvailableTimes())
        );

        scheduleRepository.save(newSchedule);
    }
    private List<TimeInterval> convertDtoToIntervals(List<AvailableIntervalDto> dtos) {
        return dtos.stream()
                .map(dto -> new TimeInterval(dto.start(), dto.end()))
                .collect(Collectors.toList());
    }


    public Schedule createSchedule() {
        var schedule = new Schedule();
        return scheduleRepository.save(schedule);
    }
}
