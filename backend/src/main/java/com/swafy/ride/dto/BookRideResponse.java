package com.swafy.ride.dto;

import com.swafy.common.enums.RideStatus;

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
