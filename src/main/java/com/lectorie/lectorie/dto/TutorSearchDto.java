package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.model.Tutor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static com.lectorie.lectorie.dto.TutorDto.getRateCount;
import static com.lectorie.lectorie.dto.TutorDto.getTutorRate;

public record TutorSearchDto(
        String tutorId,

        List<LanguageLevelDto> languageLevelDtos,

        LanguageDto languageToTeach,

        Double hourlyRate,

        String shortDescription,

        String description,

        String videoUrl,

        ScheduleDto scheduleDto,

        List<BookingDuration> allowedBookingDurations,

        List<EnrollmentDto> enrollmentDtos,

        Double avgRate,
        Integer rateCount
) {
    public static TutorSearchDto convert(Tutor from) {
        if (from == null) return null;

        return new TutorSearchDto(
                from.getId(),
                from.getLanguageLevels()
                        .stream()
                        .map(LanguageLevelDto::convert)
                        .collect(Collectors.toList()),
                (from.getLanguageToTeach() == null) ? null :
                        LanguageDto.convert(from.getLanguageToTeach()),
                from.getHourlyRate(),
                from.getShortDescription(),
                from.getDescription(),
                from.getVideoUrl(),
                ScheduleDto.convert(from.getSchedule()),
                from.getAllowedBookingDurations(),
                from.getEnrollments()
                                .stream()
                                .map(enrollment -> {
                                    try {
                                        return EnrollmentDto.convert(enrollment);
                                    } catch (DataFormatException | IOException e) {
                                        // Handle the exception as needed
                                        // For example, you can rethrow it as a runtime exception
                                        throw new RuntimeException("Error converting Enrollment to EnrollmentDto", e);
                                    }
                        })

                        .toList(),
                getTutorRate(from),
                getRateCount(from)
        );
    }
}
