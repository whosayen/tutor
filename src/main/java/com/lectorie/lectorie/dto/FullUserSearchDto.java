package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.User;

import java.io.IOException;
import java.util.zip.DataFormatException;

public record FullUserSearchDto(
        UserSearchDto userSearchDto,
        UserSettingsSearchDto userSettingsSearchDto,
        TutorSearchDto tutorSearchDto
) {

    public static FullUserSearchDto convert(User from) throws DataFormatException, IOException {
        return new FullUserSearchDto(
                UserSearchDto.convert(from),
                UserSettingsSearchDto.convert(from.getUserSettings()),
                TutorSearchDto.convert(from.getTutor())
        );
    }

    public static FullUserSearchDto convert(Tutor from) throws DataFormatException, IOException {
        User user = from.getUser();
        return convert(user);
    }

}
