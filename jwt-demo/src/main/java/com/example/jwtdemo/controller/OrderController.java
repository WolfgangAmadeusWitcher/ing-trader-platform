package com.example.jwtdemo.controller;

import com.example.jwtdemo.dto.CancelOrderRequest;
import com.example.jwtdemo.dto.CreateOrderRequest;
import com.example.jwtdemo.dto.GetOrdersBetweenDateRequest;
import com.example.jwtdemo.dto.MatchOrderRequest;
import com.example.jwtdemo.model.Order;
import com.example.jwtdemo.security.CustomUserDetails;
import com.example.jwtdemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateOrderRequest request) {
        Order createdOrder;
        createdOrder = orderService.create(request.getPrice(), request.getOrderSize(), request.getOrderSide(), request.getAssetName(), customUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody GetOrdersBetweenDateRequest request) {
        return ResponseEntity.ok().body(orderService.getOrders(customUserDetails.getUser(), request.getDateFrom(), request.getDateTo()));
    }

    @PutMapping("/order")
    public ResponseEntity<?> deleteOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CancelOrderRequest request) {
        orderService.cancelOrder(request.getOrderId(), customUserDetails.getUser());
        return ResponseEntity.ok().body("Order Cancelled Successfully!");
    }

    @PutMapping("/admin/order")
    public ResponseEntity<?> matchOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody MatchOrderRequest request) {
        orderService.matchOrder(request.getOrderId());
        return ResponseEntity.ok().body("Order Matched!");
    }
}
