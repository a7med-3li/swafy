package com.vamo.subscription.dto;

import com.vamo.common.enums.SubscriptionPlan;
import com.vamo.common.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(
        Long id,
        SubscriptionPlan plan,
        Integer totalRides,
        Integer remainingRides,
        BigDecimal price,
        LocalDate startDate,
        LocalDate endDate,
        SubscriptionStatus status,
        boolean autoRenew
) {}
