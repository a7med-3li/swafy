package com.swafy.admin.dto;

import java.util.UUID;

public record NoShowPassengerItem(
        UUID passengerId,
        String passengerName,
        int noShowCount
) {}
