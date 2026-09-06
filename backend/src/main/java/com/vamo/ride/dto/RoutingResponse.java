package com.vamo.ride.dto;

import java.math.BigDecimal;
import com.vamo.common.enums.VehicleType;

public record RoutingResponse(
	String routePolyline,
	Long duration,
	Long distance,
	VehicleType vehicleType,
	BigDecimal price
) {}
