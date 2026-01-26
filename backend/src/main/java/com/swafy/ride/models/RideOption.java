package com.swafy.ride.models;

import com.swafy.common.enums.RideType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Duration;

@Getter
@Setter
public class RideOption {
    private RideType type;
    private Duration estimatedTime;
    private BigDecimal estimatedFare;
}
