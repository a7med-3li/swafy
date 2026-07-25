package com.vamo.ride.dto;

import com.vamo.common.entity.GeoPoint;

public record RideRequestDto(GeoPoint pickUp, GeoPoint dropOff) {}
