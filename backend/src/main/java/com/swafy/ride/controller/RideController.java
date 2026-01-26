package com.swafy.ride.controller;

import com.swafy.ride.dto.RideEstimateRequestDto;
import com.swafy.ride.dto.RideEstimateResponseDto;
import com.swafy.ride.service.RideEstimationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v3/ride")
public class RideController {
    // TODO: add ride endpoints

    private final RideEstimationService rideEstimationService;

    @PostMapping("/request-ride")
    public RideEstimateResponseDto estimateRide(
            @RequestBody RideEstimateRequestDto request) {

        return rideEstimationService.estimate(request);
    }
}
