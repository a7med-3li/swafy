package com.swafy.ride.dto;

import com.swafy.common.entity.GeoPoint;

public record RideRequestDto(GeoPoint pickUp, GeoPoint dropOff) {}