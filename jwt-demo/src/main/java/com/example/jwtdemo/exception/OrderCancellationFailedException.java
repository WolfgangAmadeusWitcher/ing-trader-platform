package com.example.jwtdemo.exception;

import com.example.jwtdemo.model.Order;

public class OrderCancellationFailedException extends RuntimeException {
    public OrderCancellationFailedException(long orderId, String message) {
        super(String.format("Order Cancel Request Failed for Order: %s, reason: %s", orderId, message));
    }
}
