package com.lectorie.lectorie.exception.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {

    String message() default "Invalid name format. Usernames can only contain letters and numbers and cannot contain whitespaces.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
