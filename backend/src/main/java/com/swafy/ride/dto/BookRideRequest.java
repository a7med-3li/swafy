package com.swafy.ride.dto;

import jakarta.validation.constraints.NotNull;

public record BookRideRequest(
        @NotNull Long corridorId,
        @NotNull Long pickupVbsId,
        @NotNull Long dropoffVbsId,
        @NotNull Long subscriptionId
) {}
