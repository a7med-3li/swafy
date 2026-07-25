package com.vamo.ride.dto;

import com.vamo.common.enums.RideStatus;

import java.time.Instant;
import java.util.UUID;

public record BookRideResponse(
        UUID id,
        String pin,
        RideStatus status,
        Instant departureTime,
        Long corridorId,
        Long pickupVbsId,
        Long dropoffVbsId
) {}
