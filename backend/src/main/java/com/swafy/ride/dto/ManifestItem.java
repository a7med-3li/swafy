package com.swafy.ride.dto;

import com.swafy.common.enums.RideStatus;

import java.time.Instant;
import java.util.UUID;

public record ManifestItem(
        UUID rideId,
        UUID passengerId,
        String passengerName,
        Long pickupVbsId,
        Long dropoffVbsId,
        RideStatus status,
        Instant departureTime
) {}
