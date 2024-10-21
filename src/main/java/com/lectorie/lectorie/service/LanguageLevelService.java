package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.LanguageLevelDto;
import com.lectorie.lectorie.model.Language;
import com.lectorie.lectorie.model.LanguageLevel;
import com.lectorie.lectorie.repository.LanguageLevelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LanguageLevelService {
    private final LanguageLevelRepository repository;
    private final LanguageService languageService;

    public LanguageLevelService(LanguageLevelRepository repository, LanguageService languageService) {
        this.repository = repository;
        this.languageService = languageService;
    }

    public List<LanguageLevel> saveAllLanguages(List<LanguageLevelDto> languageLevelDtos) {
        return languageLevelDtos.stream()
                .map(this::saveLanguageLevel)
                .collect(Collectors.toList());
    }

    private LanguageLevel saveLanguageLevel(LanguageLevelDto languageLevelDto) {
        Language language = languageService.findById(languageLevelDto.languageDto().languageName());
        LanguageLevel languageLevel = new LanguageLevel(language, languageLevelDto.level());

        return repository.save(languageLevel);
    }

    public void deleteAllById(List<Long> deleteLanguageLevels) {
        repository.deleteAllById(deleteLanguageLevels);
    }



}
