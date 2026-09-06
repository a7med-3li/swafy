package com.vamo.ride.dto;

import com.vamo.common.enums.VehicleType;

public record RoutingResponse(
	String routePolyline,
	Long duration,
	Long distance,
	VehicleType vehicleType
) {}
