package com.lectorie.lectorie.dto;

public record ChatNotification(
        String id,
        UserDto userDto,
        ChatUserSettingsDto chatUserSettingsDto,
        String content
) {
}
