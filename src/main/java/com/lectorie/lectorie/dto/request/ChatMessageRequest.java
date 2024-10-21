package com.lectorie.lectorie.dto.request;

public record ChatMessageRequest(
        String recipientId,
        String content
) {
}
