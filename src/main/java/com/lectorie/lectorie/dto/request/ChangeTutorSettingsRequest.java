package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.dto.LanguageDto;
import com.lectorie.lectorie.dto.LanguageLevelDto;
import com.lectorie.lectorie.dto.ScheduleDto;
import jakarta.annotation.Nullable;

import java.util.List;

public record ChangeTutorSettingsRequest(
        @Nullable
        LanguageDto languageDto,
        @Nullable
        Double hourlyRate,
        @Nullable
        String shortDescription,
        @Nullable
        String description,
        @Nullable
        String videoUrl
) {
}
