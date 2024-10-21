package com.lectorie.lectorie.dto.request;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;

public record ChangeUserSettingsRequest(

        @Nullable
        String firstName,
        @Nullable
        String lastName,
        @Nullable
        String countryName,
        @Nullable
        String city,
        @Nullable
        String phoneCode,
        @Nullable
        String phoneNumber,
        @Nullable
        String timezone,
        @Nullable
        LocalDate dateOfBirth
) {
}
