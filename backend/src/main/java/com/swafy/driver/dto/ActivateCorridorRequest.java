package com.swafy.driver.dto;

import jakarta.validation.constraints.NotNull;

public record ActivateCorridorRequest(
        @NotNull Long corridorId,
        boolean onShift
) {}
