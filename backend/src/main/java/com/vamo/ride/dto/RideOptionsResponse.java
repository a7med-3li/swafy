package com.vamo.ride.dto;

import java.util.List;

public record RideOptionsResponse(
		List<RoutingResponse> rideOptions
) {}
