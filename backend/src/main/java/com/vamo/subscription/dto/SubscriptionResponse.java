package com.vamo.subscription.dto;

import com.vamo.common.enums.SubscriptionPlan;
import com.vamo.common.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(
        Long id,
        BigDecimal price,
        LocalDate startDate,
        LocalDate endDate,
        SubscriptionStatus status
) {}
