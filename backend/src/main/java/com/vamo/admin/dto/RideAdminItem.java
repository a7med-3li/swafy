package com.vamo.admin.dto;

import com.vamo.common.enums.RideStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RideAdminItem(
        UUID id,
        UUID passengerId,
        UUID driverId,
        Long corridorId,
        RideStatus status,
        String pin,
        Instant departureTime,
        Instant requestedAt,
        Instant startedAt,
        Instant completedAt,
        Instant boardingConfirmedAt,
        Instant noShowMarkedAt,
        BigDecimal estimatedFare,
        BigDecimal finalFare
) {}
