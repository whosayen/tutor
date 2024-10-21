package com.lectorie.lectorie.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lectorie.lectorie.config.RapidApiProperties;
import com.lectorie.lectorie.dto.LanguageDto;
import com.lectorie.lectorie.exception.custom.LanguageAlreadyExistException;
import com.lectorie.lectorie.exception.custom.LanguageDoesNotExistException;
import com.lectorie.lectorie.model.Language;
import com.lectorie.lectorie.repository.LanguageRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    private final LanguageRepository repository;
    private final RestTemplate restTemplate;
    private final RapidApiProperties rapidApiProperties;




    public LanguageService(LanguageRepository repository, RestTemplate restTemplate, RapidApiProperties rapidApiProperties) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.rapidApiProperties = rapidApiProperties;
    }

    public List<LanguageDto> getAllLanguages() {
        return repository
                .findAll()
                .stream()
                .map(LanguageDto::convert)
                .collect(Collectors.toList());
    }

    public Language findById(String languageName) {
        return repository.findById(languageName)
                .orElseThrow(() -> new LanguageDoesNotExistException("NO SUCH LANGUAGE",3990));
    }

    public LanguageDto addLanguage(String languageName, String languageCode) {
        if (repository.existsByName(languageName))
            throw new LanguageAlreadyExistException("LANGUAGE EXIST",3990);

        Language language = new Language(languageName, languageCode);
        return LanguageDto.convert(repository.save(language));
    }

    public List<Language> fetchAndSaveLanguages() {
        String url = rapidApiProperties.getBaseUrl() + "/languages";

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", rapidApiProperties.getKey());
        headers.add("x-rapidapi-host", rapidApiProperties.getHost());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Language[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Language[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Language> languages = Arrays.asList(response.getBody());

            // Save all languages to the database
            repository.saveAll(languages);

            return languages;
        } else {
            throw new RuntimeException("Failed to fetch languages: " + response.getStatusCode());
        }
    }


}
