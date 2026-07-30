package com.vamo.corridor.dto;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaveCorridorRequest(
		@NotBlank(message = "Corridor name is required")
		String name,
		
		@NotBlank(message = "Route is required")
		String route,
		
		@NotNull(message = "Price is required")
		@Positive(message = "Price must be greater than zero")
		BigDecimal price,
		
		// The nested list of stops
		@NotEmpty(message = "A corridor must have at least one stop")
		List<CorridorStopRequest> stops
) {}
