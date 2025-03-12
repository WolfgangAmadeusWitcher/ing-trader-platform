package com.example.jwtdemo.exception;

import com.example.jwtdemo.model.Order;

public class OrderFailedException extends RuntimeException {
    public OrderFailedException(String reason) {
        super(String.format("Order failed for new order request, reason: %s", reason));
    }
}
