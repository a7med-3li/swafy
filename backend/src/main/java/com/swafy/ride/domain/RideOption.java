package com.swafy.ride.domain;

import com.swafy.common.enums.RideType;

import java.math.BigDecimal;
import java.time.Duration;

public record RideOption(RideType type, Duration estimatedTime, BigDecimal estimatedFare) {}