package com.example.jwtdemo.dto;

import lombok.Getter;

public class CancelOrderRequest {

    @Getter
    private long orderId;

    public CancelOrderRequest(){

    }
    public CancelOrderRequest(long orderId) {
        this.orderId = orderId;
    }
}
