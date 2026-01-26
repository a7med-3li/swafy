package com.swafy.ride.service;

import com.swafy.ride.dto.RideResponse;
import com.swafy.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideService {
    // TODO: implement ride business logic
    private final RideRepository rideRepository;

}
