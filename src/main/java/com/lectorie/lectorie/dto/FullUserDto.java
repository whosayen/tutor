package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.User;

import java.io.IOException;
import java.util.zip.DataFormatException;

public record FullUserDto(
        UserDto userDto,
        UserSettingsDto userSettingsDto,
        TutorDto tutorDto
) {
    public static FullUserDto convert(User from) throws DataFormatException, IOException {
        return new FullUserDto(
                UserDto.convert(from),
                UserSettingsDto.convert(from.getUserSettings()),
                TutorDto.convert(from.getTutor())
        );
    }

    public static FullUserDto convert(Tutor from) throws DataFormatException, IOException {
        User user = from.getUser();

        return convert(user);
    }
}
