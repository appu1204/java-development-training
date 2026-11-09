package com.demo.payment_service.model;

public record Card (
        String cardNumber,
        String cardHolderName,
        String expiryDate
) implements PaymentMethod {}
