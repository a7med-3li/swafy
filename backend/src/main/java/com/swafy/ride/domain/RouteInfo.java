package com.swafy.ride.domain;

import java.time.Duration;

public record RouteInfo(double distanceMeters, Duration duration) {

    public static RouteInfo empty() {
        return new RouteInfo(0, Duration.ZERO);
    }
}