package com.swafy.ride.domain;

import com.swafy.common.enums.RideType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@Setter
@Builder
public class RideOption {
    private RideType type;
    private Duration estimatedTime;
    private BigDecimal estimatedFare;
}
