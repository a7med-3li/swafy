package com.swafy.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record ForfeitDepositRequest(
        @NotBlank String reason
) {}
