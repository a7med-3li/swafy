package com.swafy.ride.service;

import com.swafy.addressing.entity.Address;
import com.swafy.common.enums.RideType;
import com.swafy.ride.domain.RideOption;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.dto.RideEstimateRequestDto;
import com.swafy.ride.dto.RideEstimateResponseDto;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
// this service orchestrates other services,
    // to create and finalize a ride before saving it in DB using RideService.

@Service
@RequiredArgsConstructor
public class RideEstimationService {

    //TODO: fix this
    private final RoutingService routingService;
    public RideEstimateResponseDto estimate(RideEstimateRequestDto request) {
        ArrayList<RideOption> list = new ArrayList<>();
        list.add(estimatedFare(routingService.calculateRouteInfo(request.pickUp(), request.dropOff())));
        return new RideEstimateResponseDto(list);
    }

    public Address search(String location){
        return routingService.search(location);
    }

    private RideOption estimatedFare(RouteInfo info) {
        return new RideOption(RideType.RIDE, info.duration(), new BigDecimal("50.00"));
    }


    private void availableTypes(){}
}
