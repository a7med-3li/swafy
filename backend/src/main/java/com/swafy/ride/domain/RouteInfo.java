package com.swafy.ride.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RouteInfo {

    double distanceMeters;
    Duration duration;

    public static RouteInfo empty() {
        return new RouteInfo(0, Duration.ZERO);
    }
}
