package com.demo.payment_service.model;

public record Upi(
        String upiId
) implements PaymentMethod {}
