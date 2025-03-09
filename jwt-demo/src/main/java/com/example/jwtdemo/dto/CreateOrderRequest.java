package com.example.jwtdemo.dto;

import com.example.jwtdemo.model.OrderSide;
import lombok.Getter;

public class CreateOrderRequest {
    @Getter
    private double price;
    @Getter
    private double orderSize;
    @Getter
    private String assetName;
    @Getter
    private OrderSide orderSide;

    public CreateOrderRequest(){

    }

    public CreateOrderRequest(double price, double orderSize, String assetName, OrderSide orderSide) {
        this.price = price;
        this.orderSize = orderSize;
        this.assetName = assetName;
        this.orderSide = orderSide;
    }
}
