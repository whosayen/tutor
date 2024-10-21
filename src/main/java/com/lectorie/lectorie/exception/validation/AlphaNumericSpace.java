package com.lectorie.lectorie.exception.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphaNumericSpaceValidator.class)
public @interface AlphaNumericSpace {
    String message() default "It can only contain letters and spaces.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}