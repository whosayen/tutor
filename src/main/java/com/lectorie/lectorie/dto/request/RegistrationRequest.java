package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.dto.PasswordMatchable;
import com.lectorie.lectorie.exception.validation.MatchingPasswords;
import com.lectorie.lectorie.exception.validation.Password;
import com.lectorie.lectorie.model.Country;
import com.lectorie.lectorie.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@MatchingPasswords
public record RegistrationRequest(

        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        @Password
        String password,

        String confirmPassword,
        Role role,

        String firstName,

        String lastName,
        Country country,
        String timezone,
        LocalDate dateOfBirth

) implements PasswordMatchable {
        @Override
        public String getPassword() {
                return password;
        }

        @Override
        public String getConfirmPassword() {
                return confirmPassword;
        }
}
