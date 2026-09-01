package com.vamo.ride.service;


import java.util.List;
import com.vamo.addressing.entity.Address;
import com.vamo.common.entity.Location;
import com.vamo.ride.domain.RouteInfo;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleMapsRoutingService implements RoutingService {
    // TODO: Implement the logic

    @Override
    public RouteInfo calculateRouteInfo(Location from, Location to) {
        return null;
    }

    @Override
    public List<Address> search(String address) {
        return null;
    }
}
