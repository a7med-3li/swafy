package com.swafy.ride.service;

import com.swafy.ride.dto.RideEstimateRequestDto;
import com.swafy.ride.dto.RideEstimateResponseDto;
import org.springframework.stereotype.Service;
    // this service orchestrates other services,
    // to create and finalize a ride before saving it in DB using RideService.

@Service
public class RideEstimationService {

    public RideEstimateResponseDto estimate(RideEstimateRequestDto request){
        return new RideEstimateResponseDto();
    }
}
