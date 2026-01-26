package com.swafy.ride.service;


import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.models.RouteInfo;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleMapsRoutingService implements RoutingService {
    // TODO: Implement the logic

    @Override
    public RouteInfo calculateRoute(GeoPoint from, GeoPoint to) {
        return null;
    }
}
