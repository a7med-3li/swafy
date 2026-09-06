package com.vamo.configration.dto;

import com.vamo.common.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FareConfigResponse(
		Long id,
		VehicleType vehicleType,
		String city,
		BigDecimal baseFare,
		BigDecimal perKmRate,
		BigDecimal perMinuteRate,
		BigDecimal minimumFare,
		BigDecimal waitingRatePerMinute,
		BigDecimal surgeMultiplier,
		boolean active,
		LocalDateTime effectiveFrom,
		LocalDateTime createdAt
) {}