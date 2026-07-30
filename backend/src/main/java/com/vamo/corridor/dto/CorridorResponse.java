package com.vamo.corridor.dto;

import java.math.BigDecimal;
import java.util.List;

public record CorridorResponse(
		Long id,
		String name,
		BigDecimal price,
		List<StopResponse> stops
) {}
