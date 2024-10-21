package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.dto.LanguageDto;
import com.lectorie.lectorie.dto.LanguageLevelDto;
import jakarta.validation.constraints.Size;
import java.util.List;

// uce bolundugu icin kullanimdan kalkti
public record LanguageSettingsRequest(

        @Size(max = 10)
        List<LanguageLevelDto> languageLevelDtos,

        LanguageDto languageDto,

        Double hourlyRate
        ) {
}
