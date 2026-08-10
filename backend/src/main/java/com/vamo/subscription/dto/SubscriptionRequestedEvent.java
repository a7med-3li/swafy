package com.vamo.subscription.dto;

import com.vamo.subscription.entity.Subscription;

public record SubscriptionRequestedEvent(Subscription subscription) {}
