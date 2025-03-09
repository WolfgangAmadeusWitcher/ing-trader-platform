package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.*;
import com.example.jwtdemo.model.*;
import com.example.jwtdemo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;

    public Order create(double price, double orderSize, OrderSide orderSide, String assetName, User currentUser) throws OrderFailedException {

        if(Objects.equals(assetName, "TRY"))
            throw new OrderFailedException("YOU CAN NOT TRADE TRY, IT'S THE REFERENCE ASSET");

        Order order = new Order();
        order.setOrderSide(orderSide);
        order.setPrice(price);
        order.setAssetName(assetName);
        order.setSize(orderSize);
        order.setOwner(currentUser);

        if(orderSide.equals(OrderSide.BUY)) {
            handleBuyOrder(order);
        }else {
            handleSellOrder(order);
        }

        order.setOrderStatus(Status.PENDING);
        order.setCreateDate(new Date());
        return orderRepository.save(order);
    }

    public void cancelOrder(long orderId, User currentUser) throws OrderNotFoundException, OrderCancellationFailedException {

        Order customerOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(orderId)
        );

        CheckUserAuthorization(currentUser, customerOrder);

        if(customerOrder.getOrderStatus() != Status.PENDING) {
            throw new OrderCancellationFailedException(orderId, "ONLY ORDERS IN PENDING STATUS CAN BE CANCELLED");
        }

        Asset userTryAsset = assetService.findAssetForUser(customerOrder.getOwner(), "TRY");
        Asset userOrderAsset = assetService.findAssetForUser(customerOrder.getOwner(), customerOrder.getAssetName());
        handleOrderCancellation(customerOrder, userOrderAsset, userTryAsset);
    }

    public List<Order> getOrders(User currentUser, Date startDate, Date endDate){
        if(currentUser.getUserRole() == UserRole.ADMIN) {
            return orderRepository.findAll();
        }
        return orderRepository.findByOwner_IdAndCreateDateBetween(currentUser.getId(), startDate, endDate);
    }

    public Order matchOrder(long orderId) throws UnauthorizedException {
        Order customerOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(orderId)
        );
        Asset userTryAsset = assetService.findAssetForUser(customerOrder.getOwner(), "TRY");
        Asset userOrderAsset = assetService.findAssetForUser(customerOrder.getOwner(), customerOrder.getAssetName());

        if(customerOrder.getOrderSide().equals(OrderSide.BUY)) {
            userTryAsset.setSize(userTryAsset.getSize() - (customerOrder.getSize() * customerOrder.getPrice()));
            userOrderAsset.setUsableSize(userOrderAsset.getUsableSize() + customerOrder.getSize());
        }else {
            userTryAsset.setUsableSize(userTryAsset.getUsableSize() + (customerOrder.getSize() * customerOrder.getPrice()));
            userOrderAsset.setSize(userOrderAsset.getSize() - customerOrder.getSize());
        }
        customerOrder.setOrderStatus(Status.MATCHED);
        return orderRepository.save(customerOrder);
    }

    public void CheckUserAuthorization(User currentUser, Order order) throws UnauthorizedException {
        if(currentUser.getUserRole() != UserRole.ADMIN && order.getOwner().getId() != currentUser.getId()) {
            throw new UnauthorizedException();
        }
    }

    private void handleOrderCancellation(Order order, Asset userOrderAsset, Asset userTryAsset) {
        if(order.getOrderSide().equals(OrderSide.BUY)) {
            userOrderAsset.setSize(userOrderAsset.getSize() - order.getSize());
            userTryAsset.setUsableSize(userTryAsset.getUsableSize() + (order.getSize() * order.getPrice()));
        }else{
            userOrderAsset.setUsableSize(userOrderAsset.getUsableSize() + order.getSize());
            userTryAsset.setSize(userTryAsset.getSize() - (order.getSize() * order.getPrice()));
        }
        order.setOrderStatus(Status.CANCELED);
        orderRepository.save(order);
        assetService.updateAsset(userOrderAsset);
        assetService.updateAsset(userTryAsset);
    }

    public void handleBuyOrder(Order order) throws OrderFailedException {
        Asset userTryAsset = assetService.findAssetForUser(order.getOwner(), "TRY");
        Asset userOrderAsset = assetService.findOrCreateAssetForUser(order.getOwner(), order);

        double orderTotalPrice = order.getPrice() * order.getSize();
        CheckUserTryAssetAgainstOrder(userTryAsset, orderTotalPrice);

        userTryAsset.setUsableSize(userTryAsset.getUsableSize() - orderTotalPrice);
        userOrderAsset.setSize(userOrderAsset.getSize() + order.getSize());
        assetService.updateAsset(userOrderAsset);
        assetService.updateAsset(userTryAsset);
    }

    public void CheckUserTryAssetAgainstOrder(Asset tryAsset, double orderTotalPrice) throws OrderFailedException {
        double usableTrySize = tryAsset.getUsableSize();

        if(usableTrySize < orderTotalPrice)
        {
            throw new OrderFailedException("USABLE TRY SIZE TOO LOW");
        }
    }

    public void handleSellOrder(Order order) throws OrderFailedException {
        Asset userSellAsset = assetService.findAssetForUser(order.getOwner(), order.getAssetName());
        Asset userTryAsset = assetService.findAssetForUser(order.getOwner(), "TRY");
        double usableSellSize = userSellAsset.getUsableSize();

        if(usableSellSize < order.getSize()){
            throw new OrderFailedException(String.format("USABLE ASSET SIZE FOR %s TOO LOW", order.getAssetName()));
        }

        userSellAsset.setUsableSize(userSellAsset.getUsableSize() - order.getSize());
        userTryAsset.setSize(userTryAsset.getSize() + (order.getSize() * order.getPrice()));
        assetService.updateAsset(userSellAsset);
        assetService.updateAsset(userTryAsset);
    }
}
