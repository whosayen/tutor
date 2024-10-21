package com.lectorie.lectorie.dto.request;

import com.lectorie.lectorie.exception.validation.Password;
import jakarta.validation.constraints.Email;

public record LoginRequest(

        String email,
        String password
) {
}
