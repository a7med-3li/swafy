package com.vamo.ride.dto;

import com.vamo.common.entity.Location;

public record RideEstimateRequestDto(Location pickUp, Location dropOff) {}
