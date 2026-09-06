package com.vamo.configration.dto;

import com.vamo.common.enums.VehicleType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaveFareConfigRequest(
		@NotNull(message = "Vehicle type is required")
		VehicleType vehicleType,

		@NotBlank(message = "City/zone is required")
		String city,

		@NotNull(message = "Base fare is required")
		@Positive(message = "Base fare must be greater than zero")
		BigDecimal baseFare,

		@NotNull(message = "Per-km rate is required")
		@Positive(message = "Per-km rate must be greater than zero")
		BigDecimal perKmRate,

		@NotNull(message = "Per-minute rate is required")
		@Positive(message = "Per-minute rate must be greater than zero")
		BigDecimal perMinuteRate,

		@NotNull(message = "Minimum fare is required")
		@Positive(message = "Minimum fare must be greater than zero")
		BigDecimal minimumFare,

		@NotNull(message = "Waiting rate is required")
		@PositiveOrZero(message = "Waiting rate cannot be negative")
		BigDecimal waitingRatePerMinute,

		@NotNull(message = "Surge multiplier is required")
		@DecimalMin(value = "1.0", message = "Surge multiplier must be at least 1.0")
		BigDecimal surgeMultiplier,

		boolean active,

		@NotNull(message = "Effective date is required")
		LocalDateTime effectiveFrom
) {}