package com.demo.payment_service.dto.request;

import java.math.BigDecimal;

import com.demo.payment_service.model.PaymentMethod;

public record PaymentRequest(
        long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {}
