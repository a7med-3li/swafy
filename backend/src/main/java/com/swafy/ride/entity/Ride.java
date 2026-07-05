package com.swafy.ride.entity;

import com.swafy.common.entity.GeoPoint;
import com.swafy.common.enums.RideStatus;
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
@Table(name = "ride")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID passengerId;

    private UUID driverId;

    private Long corridorId;

    private Long subscriptionId;

    private Long pickupVbsId;

    private Long dropoffVbsId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_lng"))
    })
    private GeoPoint pickUp;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "dropoff_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dropoff_lng"))
    })
    private GeoPoint dropOff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RideStatus status;

    private BigDecimal estimatedFare;

    private BigDecimal finalFare;

    @Column(length = 4)
    private String pin;

    private Instant departureTime;

    private Instant requestedAt;

    private Instant acceptedAt;

    private Instant startedAt;

    private Instant completedAt;

    private Instant boardingConfirmedAt;

    private Instant noShowMarkedAt;
}
