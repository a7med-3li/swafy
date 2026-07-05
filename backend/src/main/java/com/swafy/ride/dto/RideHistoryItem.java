package com.swafy.ride.dto;

import com.swafy.common.enums.RideStatus;

import java.time.Instant;
import java.util.UUID;

public record RideHistoryItem(
        UUID id,
        RideStatus status,
        Long corridorId,
        Long pickupVbsId,
        Long dropoffVbsId,
        Instant departureTime,
        Instant requestedAt,
        Instant completedAt
) {}
