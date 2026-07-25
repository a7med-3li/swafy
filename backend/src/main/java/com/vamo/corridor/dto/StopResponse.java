package com.vamo.corridor.dto;

public record StopResponse(
		Long id,
		String name,
		Double latitude,
		Double longitude
) {}
