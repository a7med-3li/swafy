package com.vamo.subscription.dto;

import com.vamo.common.enums.SubscriptionPlan;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
        @NotNull SubscriptionPlan plan,
        String paymentMethod,
        String paymentReference
) {}
