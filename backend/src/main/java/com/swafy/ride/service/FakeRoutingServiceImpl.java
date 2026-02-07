package com.swafy.ride.service;

import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.service.interfaces.RoutingService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class FakeRoutingServiceImpl implements RoutingService {
    @Override
    public RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to) {
        return null;
    }
}
