package com.lectorie.lectorie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lectorie.lectorie.dto.CountryDto;
import com.lectorie.lectorie.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDto>> getAllCountries() throws JsonProcessingException {
        return ResponseEntity.ok(countryService.getAllCountries());
    }
}
