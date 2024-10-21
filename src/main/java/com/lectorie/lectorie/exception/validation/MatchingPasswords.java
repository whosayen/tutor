package com.lectorie.lectorie.exception.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchingPasswordsValidator.class)
public @interface MatchingPasswords {
    String message() default "The password incorrect. Please type the same password as in 1 field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}