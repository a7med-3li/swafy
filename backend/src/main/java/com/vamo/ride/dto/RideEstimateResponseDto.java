package com.vamo.ride.dto;

import com.vamo.ride.domain.RideOption;

import java.util.List;

public record RideEstimateResponseDto(List<RideOption> options) {}
