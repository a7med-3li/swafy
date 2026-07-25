package com.vamo.subscription.dto;

import com.vamo.common.enums.SubscriptionPlan;

import java.math.BigDecimal;

public record SubscriptionPlanInfo(
        SubscriptionPlan plan,
        String displayName,
        String description,
        BigDecimal price,
        int totalRides,
        BigDecimal pricePerRide
) {}
