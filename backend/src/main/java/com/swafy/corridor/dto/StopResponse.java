package com.swafy.corridor.dto;

public record StopResponse(
		Long id,
		String name,
		Double latitude,
		Double longitude
) {}
