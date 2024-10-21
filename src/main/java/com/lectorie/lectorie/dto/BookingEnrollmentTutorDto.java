package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.UserSettings;


public record BookingEnrollmentTutorDto(

        String firstName,
        String lastName,
        String tutorId

) {
    public static BookingEnrollmentTutorDto convert(Tutor from) {

        UserSettings us = from.getUser().getUserSettings();
        return new BookingEnrollmentTutorDto(
                us.getFirstName(),
                us.getLastName(),
                from.getId()
        );
    }
}
