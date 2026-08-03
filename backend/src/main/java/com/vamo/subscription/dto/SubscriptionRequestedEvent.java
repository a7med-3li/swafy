package com.vamo.subscription.dto;

import com.vamo.corridor.entity.Corridor;
import com.vamo.passenger.entity.PassengerProfile;
import com.vamo.user.entity.User;

public record SubscriptionRequestedEvent(PassengerProfile passenger, Corridor corridor) {
}
