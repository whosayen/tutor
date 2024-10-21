package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.CountryDto;
import com.lectorie.lectorie.dto.LanguageDto;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final CountryService countryService;
    private final LanguageService languageService;

    public AdminService(CountryService countryService, LanguageService languageService) {
        this.countryService = countryService;
        this.languageService = languageService;
    }


    public CountryDto addCountry(String countryName, String countryCode) {
        return countryService.addCountry(countryName,countryCode);
    }

    public LanguageDto addLanguage(String languageName, String languageCode) {
        return languageService.addLanguage(languageName, languageCode);
    }
}
