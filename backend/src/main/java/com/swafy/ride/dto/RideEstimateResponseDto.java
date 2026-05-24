package com.swafy.ride.dto;

import com.swafy.ride.domain.RideOption;

import java.util.List;

public record RideEstimateResponseDto(List<RideOption> options) {}