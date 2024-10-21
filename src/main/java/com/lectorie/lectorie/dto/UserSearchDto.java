package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.enums.Role;
import com.lectorie.lectorie.model.User;

public record UserSearchDto(
        String id,
        Role role
) {
    public static UserSearchDto convert(User from) {
        return new UserSearchDto(
                from.getId(),
                from.getRole()
        );
    }
}
