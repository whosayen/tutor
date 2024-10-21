package com.lectorie.lectorie.dto;

public record UserLastMessageDto(
        UserDto user,
        ChatMessageDto lastMessage
) {

}
