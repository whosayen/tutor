package com.lectorie.lectorie.dto.response;

public record PaymentResponse(
        String payment_url,
        String paymentIntentId
) {
}
