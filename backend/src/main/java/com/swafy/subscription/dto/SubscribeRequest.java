package com.swafy.subscription.dto;

import com.swafy.common.enums.SubscriptionPlan;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
        @NotNull SubscriptionPlan plan,
        String paymentMethod,
        String paymentReference
) {}
