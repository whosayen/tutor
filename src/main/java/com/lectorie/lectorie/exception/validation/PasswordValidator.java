package com.lectorie.lectorie.exception.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordValidator implements ConstraintValidator<Password, String> {
    private String uppercaseMessage;
    private String lowercaseMessage;
    private String numberMessage;
    @Override
    public void initialize(Password constraintAnnotation) {
        uppercaseMessage = constraintAnnotation.uppercaseMessage();
        lowercaseMessage = constraintAnnotation.lowercaseMessage();
        numberMessage = constraintAnnotation.numberMessage();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true; // Let not null and not blank handle this issues
        }

        if (!password.matches(".*[A-Z].*")) {
            context.buildConstraintViolationWithTemplate(uppercaseMessage)
                    .addConstraintViolation();
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            context.buildConstraintViolationWithTemplate(lowercaseMessage)
                    .addConstraintViolation();
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            context.buildConstraintViolationWithTemplate(numberMessage)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}