package com.vamo.admin.dto;

import com.vamo.common.enums.SubscriptionPlan;
import com.vamo.common.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record SubscriptionSalesItem(
        Long id,
        UUID passengerId,
        SubscriptionPlan plan,
        int totalRides,
        int remainingRides,
        BigDecimal price,
        LocalDate startDate,
        LocalDate endDate,
        SubscriptionStatus status,
        Instant createdAt,
        boolean autoRenew
) {}
