package com.vamo.ride.service.interfaces;

import com.vamo.addressing.entity.Address;
import com.vamo.common.entity.GeoPoint;
import com.vamo.ride.domain.RouteInfo;

public interface RoutingService {

    RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to);
    Address search(String location);

}
