package com.swafy.ride.service.interfaces;

import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.models.RouteInfo;

public interface RoutingService {
    RouteInfo calculateRoute(GeoPoint from, GeoPoint to);
}
