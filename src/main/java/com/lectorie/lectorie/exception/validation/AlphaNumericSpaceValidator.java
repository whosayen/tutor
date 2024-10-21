package com.lectorie.lectorie.exception.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AlphaNumericSpaceValidator implements ConstraintValidator<AlphaNumericSpace, String> {
    @Override
    public void initialize(AlphaNumericSpace constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^[a-zA-Z ]*$");
    }
}