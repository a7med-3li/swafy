package com.vamo.ride.service;


import java.util.List;
import com.vamo.addressing.entity.Address;
import com.vamo.common.entity.Location;
import com.vamo.ride.domain.RouteInfo;
import com.vamo.ride.dto.HereRouteResponse;
import com.vamo.ride.dto.RideRequestDto;
import com.vamo.ride.dto.RoutingResponse;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleMapsRoutingService implements RoutingService {
    // TODO: Implement the logic

    @Override
    public List<RoutingResponse> getRideOptions(RideRequestDto rideRequestDto) {
        return null;
    }

    @Override
    public List<Address> search(String address) {
        return null;
    }
}
