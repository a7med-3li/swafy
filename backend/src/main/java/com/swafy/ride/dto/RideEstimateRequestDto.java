package com.swafy.ride.dto;

import com.swafy.common.entity.GeoPoint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideEstimateRequestDto {
    private GeoPoint pickUp;
    private GeoPoint dropOff;
}
