package com.vamo.ride.service.interfaces;

import com.vamo.addressing.entity.Address;
import com.vamo.common.entity.Location;
import com.vamo.ride.domain.RouteInfo;

public interface RoutingService {

    RouteInfo calculateRouteInfo(Location from, Location to);
    Address search(String location);

}
