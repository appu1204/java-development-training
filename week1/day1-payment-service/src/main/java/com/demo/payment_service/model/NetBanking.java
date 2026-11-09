package com.demo.payment_service.model;

public record NetBanking(
        String bankName,
        String accountNumber,
        String ifscCode,
        String accountHolderName
) implements PaymentMethod {}
