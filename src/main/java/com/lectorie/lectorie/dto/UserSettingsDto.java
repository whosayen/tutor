package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Country;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.model.UserSettings;
import com.lectorie.lectorie.util.ImageUtil;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.zip.DataFormatException;

public record UserSettingsDto(
        String id,
        String firstName,
        String lastName,
        Country country,
        String city,
        String phoneCode,
        String phoneNumber,
        ZoneId timezone,
        LocalDate dateOfBirth,
        Integer age,
        String image
) {
    public static UserSettingsDto convert(UserSettings from) throws DataFormatException, IOException {
        return new UserSettingsDto(
                from.getId(),
                from.getFirstName(),
                from.getLastName(),
                from.getCountry(),
                from.getCity(),
                from.getPhoneCode(),
                from.getPhoneNumber(),
                from.getTimezone(),
                from.getDateOfBirth(),
                from.getAge(),
                (from.getImageData() != null) ?
                        Base64.getEncoder().encodeToString(ImageUtil.decompressImage(from.getImageData())) : null
        );
    }
}