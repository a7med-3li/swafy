package com.vamo.ride.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HereRouteResponse(List<Route> routes){
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Route(List<Section> sections) {}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Section(Summary summary, String polyline) {}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Summary(
			int length,
			int duration,   // ETA in seconds (with traffic)
			int baseDuration    // ETA in seconds (without traffic)
	) {}
	
	/**
	 * Calculates the total ETA in seconds across all route sections (waypoints).
	 */
	public int getTotalDurationSeconds() {
		if (routes == null || routes.isEmpty() || routes.get(0).sections() == null) {
			return 0;
		}
		return routes.get(0).sections().stream()
				.mapToInt(section -> section.summary().duration())
				.sum();
	}
	
	/**
	 * Calculates the total distance in meters across all route sections.
	 */
	public int getTotalDistanceMeters() {
		if (routes == null || routes.isEmpty() || routes.get(0).sections() == null) {
			return 0;
		}
		return routes.get(0).sections().stream()
				.mapToInt(section -> section.summary().length())
				.sum();
	}
}
