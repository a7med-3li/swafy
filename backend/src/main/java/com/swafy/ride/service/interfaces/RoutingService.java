package com.swafy.ride.service.interfaces;

import com.swafy.addressing.entity.Address;
import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;

public interface RoutingService {

    RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to);
    Address search(String location);

}
