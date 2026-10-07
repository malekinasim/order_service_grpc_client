package com.example.order.servie;

import com.example.payment.PaymentRequest;
import com.example.payment.PaymentResponse;
import com.example.payment.PaymentServiceGrpc;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    private final PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub;

    public OrderService(
            PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub) {
        this.paymentStub = paymentStub;
    }

    public PaymentResponse payOrder(
            long orderId, long amountMinor, String currency,String idempotencyKey) {

        PaymentRequest request = PaymentRequest.newBuilder()
                .setOrderId(orderId)
                .setAmountMinor(amountMinor)
                .setCurrency(currency)
                .setIdempotencyKey(idempotencyKey)
                .build();

        return paymentStub
                .withDeadlineAfter(3, TimeUnit.SECONDS)
                .pay(request);
    }
}