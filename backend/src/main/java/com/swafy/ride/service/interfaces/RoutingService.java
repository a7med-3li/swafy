package com.swafy.ride.service.interfaces;

import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;

import java.time.Duration;

public interface RoutingService {

    RouteInfo calculateRoute(GeoPoint from, GeoPoint to);

//    double calculateDistanceKM(GeoPoint from, GeoPoint to);
//
//    Duration calculateETA(GeoPoint from, GeoPoint to);
}
