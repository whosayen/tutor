package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.Language;

public record LanguageDto(
        String languageName,
        String code
) {

    public static LanguageDto convert(Language from) {
        return new LanguageDto(from.getName(), from.getCode());
    }

}
