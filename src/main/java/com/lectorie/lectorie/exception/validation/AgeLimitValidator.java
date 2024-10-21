package com.lectorie.lectorie.exception.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, LocalDate> {

    private int ageLimit;

    @Override
    public void initialize(AgeLimit constraintAnnotation) {
        ageLimit = constraintAnnotation.ageLimit();
    }
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true; // Allow null values to be handled by other constraints
        }

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears() >= ageLimit;
    }
}