package com.swafy.ride.service;

import com.swafy.addressing.entity.Address;
import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
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
