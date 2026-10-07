package com.example.order.model;

public record OrderRequest(Long amount,String currency,String key) {
}
