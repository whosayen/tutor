package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Enrollment;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.enums.BookingDuration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public record TutorDto(
        String tutorId,

        List<LanguageLevelDto> languageLevelDtos,

        LanguageDto languageToTeach,

        Double hourlyRate,

        String shortDescription,

        String description,

        String videoUrl,

        ScheduleDto scheduleDto,

        Boolean isApproved,
        List<EnrollmentDto> enrollmentDtos,

        List<BookingDuration> allowedBookingDurations,

        Double avgRate,
        Integer rateCount

    ) {

    public static TutorDto convert(Tutor from) {
        if (from == null) return null;

        return new TutorDto(
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
                from.isApproved(),
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
                from.getAllowedBookingDurations(),
                getTutorRate(from),
                getRateCount(from)
        );
    }

    public static double getTutorRate(Tutor tutor) {
        double totalRate = 0;
        int count = 0;
        for (Enrollment er : tutor.getEnrollments()) {
            if (er.getRate() != null) {
                totalRate += er.getRate();
                count++;
            }
        }

        return totalRate / count;
    }

    public static int getRateCount(Tutor tutor) {
        int count = 0;
        for (Enrollment er : tutor.getEnrollments()) {
            if (er.getRate() != null) {
                count++;
            }
        }

        return count;
    }
}
