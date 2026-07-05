package com.swafy.driver.entity;

import com.swafy.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "driver_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID driverProfileId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAfter;

    private String reference;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Instant createdAt;
}
