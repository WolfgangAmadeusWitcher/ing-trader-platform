package com.example.jwtdemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="Assets")
public class Asset {

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
    @Column(nullable = false)
    private BigDecimal size;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal usableSize;
}
