package com.vamo.ride.dto;

import com.vamo.common.entity.GeoPoint;

public record RideEstimateRequestDto(GeoPoint pickUp, GeoPoint dropOff) {}
