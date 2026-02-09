package com.swafy.ride.service;

import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Primary
@RequiredArgsConstructor
public class FakeRoutingServiceImpl implements RoutingService {
    // TODO: fix this

    private final RouteInfo routeInfo;

    @Override
    public RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to) {
        routeInfo.setDistanceKm(500);
        routeInfo.setDuration(Duration.ofHours((long) (routeInfo.getDistanceKm()/60)));
        return routeInfo;
    }
}
