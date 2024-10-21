package com.lectorie.lectorie.exception.validation;

import com.lectorie.lectorie.dto.PasswordMatchable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingPasswordsValidator implements ConstraintValidator<MatchingPasswords, PasswordMatchable> {

    @Override
    public void initialize(MatchingPasswords constraintAnnotation) {
    }

    @Override
    public boolean isValid(PasswordMatchable request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        return request.getPassword().equals(request.getConfirmPassword());
    }
}

