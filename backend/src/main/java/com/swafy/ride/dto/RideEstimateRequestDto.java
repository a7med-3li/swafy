package com.swafy.ride.dto;

import com.swafy.common.entity.GeoPoint;

public record RideEstimateRequestDto(GeoPoint pickUp, GeoPoint dropOff) {}