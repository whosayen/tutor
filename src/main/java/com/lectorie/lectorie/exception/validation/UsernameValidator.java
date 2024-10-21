package com.lectorie.lectorie.exception.validation;

import com.lectorie.lectorie.exception.validation.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true; // Let not null and not blank handle this issues
        }
        return pattern.matcher(username).matches();
    }
}
