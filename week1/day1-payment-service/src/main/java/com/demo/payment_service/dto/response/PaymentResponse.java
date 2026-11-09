package com.demo.payment_service.dto.response;

import com.demo.payment_service.model.PaymentStatus;

public record PaymentResponse(
        String message,
        PaymentStatus status,
        long paymentId
) {}