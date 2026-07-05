package com.swafy.subscription.dto;

import com.swafy.common.enums.SubscriptionPlan;

import java.math.BigDecimal;

public record SubscriptionPlanInfo(
        SubscriptionPlan plan,
        String displayName,
        String description,
        BigDecimal price,
        int totalRides,
        BigDecimal pricePerRide
) {}
