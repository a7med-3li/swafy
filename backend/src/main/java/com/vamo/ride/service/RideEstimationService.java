package com.vamo.ride.service;

import com.vamo.addressing.entity.Address;
import com.vamo.common.enums.RideType;
import com.vamo.ride.domain.RideOption;
import com.vamo.ride.domain.RouteInfo;
import com.vamo.ride.dto.RideEstimateRequestDto;
import com.vamo.ride.dto.RideEstimateResponseDto;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
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
