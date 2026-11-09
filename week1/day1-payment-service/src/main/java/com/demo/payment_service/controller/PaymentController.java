package com.demo.payment_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.payment_service.dto.request.PaymentRequest;
import com.demo.payment_service.dto.response.PaymentResponse;
import com.demo.payment_service.service.PaymentService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping ("/api/v1/payments")
@RequiredArgsConstructor 

public class PaymentController {

    private final PaymentService paymentService;
    // POST -> api/v1/payments

    @PostMapping
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }
    
}