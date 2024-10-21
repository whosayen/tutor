package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.UserSettings;

import java.time.ZoneId;

public record BookingEnrollmentStudentDto(
        String id,
        String firstName,
        String lastName,
        CountryDto countryDto,
        String phoneCode,
        String phoneNumber,
        ZoneId timezone,
        UserDto userDto,
        Integer age

) {
    public static BookingEnrollmentStudentDto convert(UserSettings from) {
        return new BookingEnrollmentStudentDto(
                from.getId(),
                from.getFirstName(),
                from.getLastName(),
                CountryDto.convert(from.getCountry()),
                from.getPhoneCode(),
                from.getPhoneNumber(),
                from.getTimezone(),
                UserDto.convert(from.getUser()),
                from.getAge()
        );
    }
}
