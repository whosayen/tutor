package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.CountryDto;
import com.lectorie.lectorie.dto.LanguageDto;
import com.lectorie.lectorie.dto.TutorDto;
import com.lectorie.lectorie.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("add-country")
    public ResponseEntity<CountryDto> addCountry(@RequestParam String countryName, @RequestParam String countryCode){
        return ResponseEntity.ok(adminService.addCountry(countryName,countryCode));
    }

    @PostMapping("add-language")
    public ResponseEntity<LanguageDto> addLanguage(@RequestParam String languageName, @RequestParam String languageCode) {
        return ResponseEntity.ok(adminService.addLanguage(languageName, languageCode));
    }
}
