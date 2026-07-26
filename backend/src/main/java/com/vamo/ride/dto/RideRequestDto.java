package com.vamo.ride.dto;

import com.vamo.common.entity.Location;

public record RideRequestDto(Location pickUp, Location dropOff) {}
