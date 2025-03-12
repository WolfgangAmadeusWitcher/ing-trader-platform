package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.*;
import com.example.jwtdemo.model.*;
import com.example.jwtdemo.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;

    @Transactional
    public Order create(BigDecimal price, BigDecimal orderSize, OrderSide orderSide, String assetName, User currentUser) throws OrderFailedException {

        if(Objects.equals(assetName, "TRY"))
            throw new OrderFailedException("YOU CAN NOT TRADE TRY, IT'S THE REFERENCE ASSET");

        if(orderSize.compareTo(BigDecimal.ZERO) <= 0)
            throw new OrderFailedException("YOU CAN NOT TRADE ZERO/NEGATIVE ORDER SIZE");

        if(price.compareTo(BigDecimal.ZERO) <= 0)
            throw new OrderFailedException("YOU CAN NOT TRADE WITH ZERO/NEGATIVE PRICE");

        Order order = new Order();
        order.setOrderSide(orderSide);
        order.setPrice(price);
        order.setAssetName(assetName);
        order.setSize(orderSize);
        order.setOwner(currentUser);

        if(orderSide.equals(OrderSide.BUY)) {
            processBuyOrder(order);
        }else {
            processSellOrder(order);
        }

        order.setOrderStatus(Status.PENDING);
        order.setCreateDate(new Date());
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(long orderId, User currentUser) throws OrderNotFoundException, OrderCancellationFailedException {

        Order customerOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(orderId)
        );

        CheckUserAuthorization(currentUser, customerOrder);

        if(customerOrder.getOrderStatus() != Status.PENDING) {
            throw new OrderCancellationFailedException(orderId, "ONLY ORDERS IN PENDING STATUS CAN BE CANCELLED");
        }

        processOrderCancellation(customerOrder);
    }

    @Transactional
    public Order matchOrder(long orderId) throws UnauthorizedException {
        Order customerOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(orderId)
        );
        Asset userTryAsset = assetService.findAssetForUser(customerOrder.getOwner(), "TRY");
        Asset userOrderAsset = assetService.findAssetForUser(customerOrder.getOwner(), customerOrder.getAssetName());

        if(customerOrder.getOrderSide().equals(OrderSide.BUY)) {
            userTryAsset.setSize(userTryAsset.getSize().subtract(calculateTotalPrice(customerOrder)));
            userOrderAsset.setUsableSize(userOrderAsset.getUsableSize().add(customerOrder.getSize()));
        }else {
            userTryAsset.setUsableSize(userTryAsset.getUsableSize().add((calculateTotalPrice(customerOrder))));
            userOrderAsset.setSize(userOrderAsset.getSize().subtract(customerOrder.getSize()));
        }
        customerOrder.setOrderStatus(Status.MATCHED);
        return orderRepository.save(customerOrder);
    }

    public List<Order> getOrders(User currentUser, Date startDate, Date endDate){
        if(currentUser.getUserRole() == UserRole.ADMIN) {
            return orderRepository.findAll();
        }

        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Input Dates cannot be null");
        }

        if (startDate.after(endDate)) {
            throw new InvalidDateException("Start Date cannot be after End Date");
        }

        return orderRepository.findByOwner_IdAndCreateDateBetween(currentUser.getId(), startDate, endDate);
    }

    public void processBuyOrder(Order order) throws OrderFailedException {
        Asset userTryAsset = assetService.findAssetForUser(order.getOwner(), "TRY");
        Asset userOrderAsset = assetService.findOrCreateAssetForUser(order.getOwner(), order.getAssetName());

        BigDecimal orderTotalPrice = calculateTotalPrice(order);
        validateSufficientFunds(userTryAsset, orderTotalPrice);

        userTryAsset.setUsableSize(userTryAsset.getUsableSize().subtract(orderTotalPrice));
        userOrderAsset.setSize(userOrderAsset.getSize().add(order.getSize()));
        assetService.updateAsset(userOrderAsset);
        assetService.updateAsset(userTryAsset);
    }

    public void processSellOrder(Order order) throws OrderFailedException {
        Asset userSellAsset = assetService.findAssetForUser(order.getOwner(), order.getAssetName());
        Asset userTryAsset = assetService.findAssetForUser(order.getOwner(), "TRY");

        validateSufficientAsset(userSellAsset, order.getSize());

        userSellAsset.setUsableSize(userSellAsset.getUsableSize().subtract(order.getSize()));
        userTryAsset.setSize(userTryAsset.getSize().add(calculateTotalPrice(order)));
        assetService.updateAsset(userSellAsset);
        assetService.updateAsset(userTryAsset);
    }

    private void processOrderCancellation(Order order){
        Asset userTryAsset = assetService.findAssetForUser(order.getOwner(), "TRY");
        Asset userOrderAsset = assetService.findAssetForUser(order.getOwner(), order.getAssetName());

        if(order.getOrderSide().equals(OrderSide.BUY)) {
            userOrderAsset.setSize(userOrderAsset.getSize().subtract(order.getSize()));
            userTryAsset.setUsableSize(userTryAsset.getUsableSize().add(calculateTotalPrice(order)));
        }else{
            userOrderAsset.setUsableSize(userOrderAsset.getUsableSize().add(order.getSize()));
            userTryAsset.setSize(userTryAsset.getSize().subtract(calculateTotalPrice(order)));
        }
        order.setOrderStatus(Status.CANCELED);
        orderRepository.save(order);
        assetService.updateAsset(userOrderAsset);
        assetService.updateAsset(userTryAsset);
    }

    private BigDecimal calculateTotalPrice(Order order) {
        return order.getPrice().multiply(order.getSize());
    }

    public void validateSufficientAsset(Asset userOrderAsset, BigDecimal userOrderAssetSize){
        if(userOrderAsset.getUsableSize().compareTo(userOrderAssetSize) < 0) {
            throw new OrderFailedException("USABLE ASSET SIZE TOO LOW");
        }
    }

    public void validateSufficientFunds(Asset tryAsset, BigDecimal orderTotalPrice) throws OrderFailedException {
        if(tryAsset.getUsableSize().compareTo(orderTotalPrice) < 0)
            throw new OrderFailedException("USABLE TRY ASSET SIZE TOO LOW");
    }

    public void CheckUserAuthorization(User currentUser, Order order) throws UnauthorizedException {
        if(currentUser.getUserRole() != UserRole.ADMIN && order.getOwner().getId() != currentUser.getId()) {
            throw new UnauthorizedException();
        }
    }
}
