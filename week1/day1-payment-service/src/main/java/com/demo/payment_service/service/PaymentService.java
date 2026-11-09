package com.demo.payment_service.service;

import com.demo.payment_service.dto.request.PaymentRequest;
import com.demo.payment_service.dto.response.PaymentResponse;

public interface PaymentService {
    public PaymentResponse processPayment(PaymentRequest request);
}
