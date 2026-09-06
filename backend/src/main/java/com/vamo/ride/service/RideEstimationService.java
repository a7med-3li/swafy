package com.vamo.ride.service;

import com.vamo.addressing.entity.Address;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideEstimationService {
    private final RoutingService routingService;


    // note: this method will need a huge optimization, just take care will implementing it, for now it is just a placeholder
    private void availableTypes(){}
}
