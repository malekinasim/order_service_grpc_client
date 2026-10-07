package com.example.order.exception;

public record ApiError(String paymentUnavailable, String paymentServiceIsUnavailable) {
}
