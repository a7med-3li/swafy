package com.vamo.ride.service;


import com.vamo.addressing.entity.Address;
import com.vamo.common.entity.GeoPoint;
import com.vamo.ride.domain.RouteInfo;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleMapsRoutingService implements RoutingService {
    // TODO: Implement the logic

    @Override
    public RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to) {
        return null;
    }

    @Override
    public Address search(String address) {
        return null;
    }
}
