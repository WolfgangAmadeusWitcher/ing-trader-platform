package com.example.jwtdemo.dto;

import com.example.jwtdemo.model.OrderSide;
import lombok.Getter;

import java.math.BigDecimal;

public class CreateOrderRequest {
    @Getter
    private BigDecimal price;
    @Getter
    private BigDecimal orderSize;
    @Getter
    private String assetName;
    @Getter
    private OrderSide orderSide;

    public CreateOrderRequest(){

    }

    public CreateOrderRequest(BigDecimal price, BigDecimal orderSize, String assetName, OrderSide orderSide) {
        this.price = price;
        this.orderSize = orderSize;
        this.assetName = assetName;
        this.orderSide = orderSide;
    }
}
