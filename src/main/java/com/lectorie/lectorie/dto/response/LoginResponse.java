package com.lectorie.lectorie.dto.response;

import com.lectorie.lectorie.enums.Role;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Role role,
        String id
) {
}
