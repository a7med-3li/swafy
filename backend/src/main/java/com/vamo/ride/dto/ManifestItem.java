package com.vamo.ride.dto;

import com.vamo.common.enums.RideStatus;

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
