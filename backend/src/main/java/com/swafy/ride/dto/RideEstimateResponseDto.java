package com.swafy.ride.dto;

import com.swafy.ride.models.RideOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RideEstimateResponseDto {
    private List<RideOption> options;
}
