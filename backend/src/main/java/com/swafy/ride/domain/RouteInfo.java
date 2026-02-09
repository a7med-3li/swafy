package com.swafy.ride.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Builder
public class RouteInfo {

    double distanceKm;
    Duration duration;
}
