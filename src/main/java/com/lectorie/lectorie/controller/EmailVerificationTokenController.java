package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.service.EmailVerificationTokenService;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email-tokens")
@Validated
public class EmailVerificationTokenController {

    private final EmailVerificationTokenService emailVerificationTokenService;

    public EmailVerificationTokenController(EmailVerificationTokenService emailVerificationTokenService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createToken(@RequestParam @Email String email) {
        return new ResponseEntity<>(emailVerificationTokenService.sendEmailVerification(email), HttpStatus.CREATED);
    }

    @PutMapping("/check-otp")
    public ResponseEntity<String> checkOtp(@RequestParam @Email String email, @RequestParam String otp) {
        return new ResponseEntity<>(emailVerificationTokenService.checkOtp(email, otp), HttpStatus.ACCEPTED);
    }
}
