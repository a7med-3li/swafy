package com.swafy.ride.entity;

import com.swafy.common.enums.RideStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "rides")
public class Ride {

    //TODO: manage the relation between tables

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private UUID passengerId;
    private UUID driverId;

    private Long lat;
    private Long lon;

    private RideStatus status;

    private BigDecimal estimatedFare;
    private BigDecimal finalFare;

    // --- Timestamps ---
    private Instant requestedAt;
    private Instant acceptedAt;
    private Instant startedAt;
    private Instant completedAt;

}
