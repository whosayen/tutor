package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.LanguageLevel;
import com.lectorie.lectorie.enums.Level;

public record LanguageLevelDto(
        LanguageDto languageDto,
        Level level
) {
    public static LanguageLevelDto convert(LanguageLevel from) {
        return new LanguageLevelDto(
                LanguageDto.convert(from.getLanguage()),
                from.getLevel()
        );
    }
}
