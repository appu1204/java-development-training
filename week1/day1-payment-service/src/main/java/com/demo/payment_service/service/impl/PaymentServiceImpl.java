package com.demo.payment_service.service.impl;


import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.demo.payment_service.dto.request.PaymentRequest;
import com.demo.payment_service.dto.response.PaymentResponse;

import com.demo.payment_service.model.Card;
import com.demo.payment_service.model.NetBanking;
import com.demo.payment_service.model.PaymentMethod;
import com.demo.payment_service.model.PaymentStatus;
import com.demo.payment_service.model.Upi;
import com.demo.payment_service.service.PaymentService;

@Service 
public class PaymentServiceImpl implements PaymentService {

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override 
    public PaymentResponse processPayment(PaymentRequest request) {
        PaymentMethod paymentMethod = request.paymentMethod();  
        BigDecimal amount = request.amount();

        PaymentStatus status = switch(paymentMethod) {
            case Card c      -> processCard(c, amount);
            case Upi u        -> processUpi(u, amount);
            case NetBanking n -> processNetBanking(n, amount);
        };
        
        long paymentId = idGenerator.getAndIncrement();
        String message = (status == PaymentStatus.SUCCESS)
                ? "Payment completed"
                : "Payment failed";

        return new PaymentResponse(message, status, paymentId);
    }


    public PaymentStatus processCard(Card card, BigDecimal amount) {
        if (card.cardNumber() == null || card.cardNumber().length() < 12) {
            return PaymentStatus.FAILED;
        }
        return PaymentStatus.SUCCESS;
    }

    public PaymentStatus processUpi(Upi upi, BigDecimal amount) {
        if (upi.upiId() == null || !upi.upiId().contains("@")) {
            return PaymentStatus.FAILED;
        }
        return PaymentStatus.SUCCESS;
    }

    public PaymentStatus processNetBanking(NetBanking netBanking, BigDecimal amount) {
        if (netBanking.accountNumber() == null || netBanking.accountNumber().length() < 10) {
                return PaymentStatus.FAILED;
            }
        return PaymentStatus.SUCCESS;
    }
}
