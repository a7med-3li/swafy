package com.vamo.corridor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CorridorStopRequest(
		Long id,
		
		@NotBlank(message = "Stop name is required")
		String name,
		
		@NotNull(message = "Latitude is required")
		Double latitude,
		
		@NotNull(message = "Longitude is required")
		Double longitude
) {}
