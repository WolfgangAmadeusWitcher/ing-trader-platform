package com.example.jwtdemo.repository;

import com.example.jwtdemo.model.Order;
import com.example.jwtdemo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOwner_IdAndCreateDateBetween(long ownerId, Date createDateAfter, Date createDateBefore);
    List<Order> findByOwner_IdAndOrderStatus(long ownerId, Status orderStatus);
    Optional<Order> findByOwner_IdAndId(long ownerId, long orderId);
    Optional<Order> findById(long id);
}
