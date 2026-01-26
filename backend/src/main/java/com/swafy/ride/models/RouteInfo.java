package com.swafy.ride.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RouteInfo {

    double distanceKm;
    Duration duration;
}
