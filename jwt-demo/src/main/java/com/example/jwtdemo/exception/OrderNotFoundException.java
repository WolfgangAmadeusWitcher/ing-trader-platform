package com.example.jwtdemo.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(long orderId) {
        super(String.format("Order with id: %s, Not Found!", orderId));
    }
}
