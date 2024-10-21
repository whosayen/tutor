package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Enrollment;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

public record EnrollmentDto(
    String id,
    Integer rate,
    String comment,
    List<EnrollmentBookingDto> bookings,
    String tutorId,
    UserSettingsSearchDto userSettings
) {
    public static EnrollmentDto convert(Enrollment from) throws DataFormatException, IOException {
        return new EnrollmentDto(
                from.getId(),
                from.getRate(),
                from.getComment(),
                from.getBookings()
                        .stream()
                        .map(EnrollmentBookingDto::convert)
                        .toList(),
                from.getTutor().getId(),
                UserSettingsSearchDto.convert(from.getUserSettings())
        );
    }
}
