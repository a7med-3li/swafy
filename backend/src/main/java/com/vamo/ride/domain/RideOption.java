package com.vamo.ride.domain;

import com.vamo.common.enums.RideType;

import java.math.BigDecimal;
import java.time.Duration;

public record RideOption(RideType type, Duration estimatedTime, BigDecimal estimatedFare) {}
