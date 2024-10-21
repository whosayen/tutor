package com.lectorie.lectorie.dto;


import com.lectorie.lectorie.enums.Role;
import com.lectorie.lectorie.model.User;

public record UserDto(
        String id,
        Role role,
        String email,
        Boolean isEnabled,
        Boolean isCredentialsNonExpired,
        Boolean isAccountNonExpired,
        Boolean isAccountNonLocked,
        Double balance
) {
    public static UserDto convert(User from) {
        return new UserDto(
                from.getId(),
                from.getRole(),
                from.getEmail(),
                from.isEnabled(),
                from.isCredentialsNonExpired(),
                from.isAccountNonExpired(),
                from.isAccountNonLocked(),
                from.getBalance()
        );
    }

}
