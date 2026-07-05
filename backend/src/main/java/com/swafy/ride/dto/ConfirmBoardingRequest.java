package com.swafy.ride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ConfirmBoardingRequest(
        @NotNull UUID rideId,
        @NotBlank @Size(min = 4, max = 4) String pin
) {}
