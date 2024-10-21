package com.lectorie.lectorie.dto;

import com.lectorie.lectorie.model.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageDto (
        String id,
        String chatId,
        String senderId,
        String recipientId,
        String senderName,
        String recipientName,
        String content,
        LocalDateTime timestamp) {
    public static ChatMessageDto convert (ChatMessage chatMessage) {
        return new ChatMessageDto(
                chatMessage.getId(),
                chatMessage.getChatId(),
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                chatMessage.getSenderName(),
                chatMessage.getRecipientName(),
                chatMessage.getContent(),
                chatMessage.getTimestamp()
        );
    }
}
