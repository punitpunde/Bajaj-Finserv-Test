package com.bajaj_test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENTS")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "EMP_ID", referencedColumnName = "EMP_ID")
    private Employee employee;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "PAYMENT_TIME")
    private LocalDateTime paymentTime;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
