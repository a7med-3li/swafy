package com.vamo.corridor.dto;

import java.util.List;

public record CorridorResponse(
		Long id,
		String name,
		Double price,
		List<StopResponse> stops
) {}
