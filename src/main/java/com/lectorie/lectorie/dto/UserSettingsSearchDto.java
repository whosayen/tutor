package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.UserSettings;
import com.lectorie.lectorie.util.ImageUtil;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Base64;
import java.util.zip.DataFormatException;

public record UserSettingsSearchDto(
        String id,
        String firstName,
        String lastName,
        CountryDto countryDto,
        String city,
        ZoneId timezone,
        Integer age,
        String image
) {
    public static UserSettingsSearchDto convert(UserSettings from) throws DataFormatException, IOException {
        return new UserSettingsSearchDto(
                from.getId(),
                from.getFirstName(),
                from.getLastName(),
                CountryDto.convert(from.getCountry()),
                from.getCity(),
                from.getTimezone(),
                from.getAge(),
                (from.getImageData() != null) ?
                        Base64.getEncoder().encodeToString(ImageUtil.decompressImage(from.getImageData())) : null

        );
    }

}
