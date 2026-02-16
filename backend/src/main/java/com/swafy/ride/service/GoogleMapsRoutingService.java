package com.swafy.ride.service;


import com.swafy.addressing.entity.Address;
import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
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
