package com.example.jwtdemo.dto;

import lombok.Getter;

public class MatchOrderRequest {
    @Getter
    private long orderId;

    public MatchOrderRequest(){

    }
    public MatchOrderRequest(long orderId) {
        this.orderId = orderId;
    }
}
