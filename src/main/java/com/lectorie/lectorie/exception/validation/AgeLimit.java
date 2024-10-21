package com.lectorie.lectorie.exception.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeLimitValidator.class)
public @interface AgeLimit {
    String message() default "User must be at least {ageLimit} years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int ageLimit() default 18; // Default age limit is 18 years
}

