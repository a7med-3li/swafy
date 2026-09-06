package com.vamo.admin.dto;

import com.vamo.common.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PendingSubscriptionItem(
		Long id,
		UUID passengerId,
		String passengerName,
		String passengerPhone,
		Long corridorId,
		String corridorTitle,
		BigDecimal price,
		LocalDate startDate,
		LocalDate endDate,
		SubscriptionStatus status
) {}