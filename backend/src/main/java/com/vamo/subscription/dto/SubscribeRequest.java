package com.vamo.subscription.dto;

import com.vamo.corridor.entity.Corridor;

public record SubscribeRequest(
        Corridor corridor
) {}
