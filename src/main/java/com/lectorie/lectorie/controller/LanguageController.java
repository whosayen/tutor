package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.LanguageDto;
import com.lectorie.lectorie.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/language")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<LanguageDto>> getAllLanguage(){
        return ResponseEntity.ok(languageService.getAllLanguages());
    }
}
