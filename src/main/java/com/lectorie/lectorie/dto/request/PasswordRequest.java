package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.dto.PasswordMatchable;
import com.lectorie.lectorie.exception.validation.MatchingPasswords;
import com.lectorie.lectorie.exception.validation.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@MatchingPasswords
public record PasswordRequest(

        String oldPassword,

        @NotBlank
        @Size(min = 8, max = 20)
        @Password
        String newPassword,

        String confirmPassword

) implements PasswordMatchable {
    @Override
    public String getPassword() {
        return newPassword;
    }

    @Override
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
