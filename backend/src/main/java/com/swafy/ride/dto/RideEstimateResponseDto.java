package com.swafy.ride.dto;

import com.swafy.ride.domain.RideOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RideEstimateResponseDto {
    private List<RideOption> options;
}
