package com.vamo.ride.service;

import com.vamo.addressing.entity.Address;
import com.vamo.common.entity.GeoPoint;
import com.vamo.ride.domain.RouteInfo;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class FakeRoutingServiceImpl implements RoutingService {
    // TODO: fix this

    @Override
    public RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to) {
        return new RouteInfo(500, Duration.ofHours(500 / 60));
    }

    @Override
    public Address search(String address) {
        return null;
    }
}
