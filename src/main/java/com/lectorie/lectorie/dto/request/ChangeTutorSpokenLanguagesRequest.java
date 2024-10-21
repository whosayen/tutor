package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.dto.LanguageLevelDto;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChangeTutorSpokenLanguagesRequest(
        @Size(max = 10)
        List<LanguageLevelDto> languageLevelDtos

        ) {
}
