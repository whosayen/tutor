package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Enrollment;

public record BookingEnrollmentDto(
        String id,
        Integer rate,
        String comment,
        BookingEnrollmentTutorDto bookingEnrollmentTutorDto,
        BookingEnrollmentStudentDto bookingEnrollmentStudentDto


) {

    public static BookingEnrollmentDto convert(Enrollment from) {
        return new BookingEnrollmentDto(
                from.getId(),
                from.getRate(),
                from.getComment(),
                BookingEnrollmentTutorDto.convert(from.getTutor()),
                BookingEnrollmentStudentDto.convert(from.getUserSettings())
        );
    }
}
