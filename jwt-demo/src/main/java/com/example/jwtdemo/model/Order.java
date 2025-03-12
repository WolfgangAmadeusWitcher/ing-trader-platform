package com.example.jwtdemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="Orders")
public class Order {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Getter
    @Setter
    @Column(nullable = false)
    private String assetName;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide orderSide;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status orderStatus;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal size;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal price;

    @Getter
    @Setter
    @Column(nullable = false)
    private Date createDate;
}
