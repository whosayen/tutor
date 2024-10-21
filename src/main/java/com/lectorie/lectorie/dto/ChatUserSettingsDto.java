package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Country;
import com.lectorie.lectorie.model.UserSettings;
import com.lectorie.lectorie.util.ImageUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.zip.DataFormatException;

public record ChatUserSettingsDto(
        String id,
        String firstName,
        String lastName,
        Country country,
        String city,
        String phoneCode,
        String phoneNumber,
        LocalDate dateOfBirth,
        Integer age,
        String image
) {

    public static ChatUserSettingsDto convert(UserSettings from) throws DataFormatException, IOException {

        return new ChatUserSettingsDto(
                from.getId(),
                from.getFirstName(),
                from.getLastName(),
                from.getCountry(),
                from.getCity(),
                from.getPhoneCode(),
                from.getPhoneNumber(),
                from.getDateOfBirth(),
                from.getAge(),
                (from.getImageData() != null) ?
                        Base64.getEncoder().encodeToString(ImageUtil.decompressImage(from.getImageData())) : null
        );
    }

}
