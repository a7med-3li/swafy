package com.swafy.ride.dto;

import com.swafy.common.entity.GeoPoint;
import com.swafy.common.enums.RideStatus;
import com.swafy.common.enums.RideType;
import com.swafy.common.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RideInitializationRequest {

    private UUID userId;
    private UUID driverId;
    private RideType rideType;
    private Long finalFare;
    private RideStatus status = RideStatus.REQUESTED;
    private VehicleType vehicleType;
    private GeoPoint pickUp;
    private GeoPoint dropOff;
}
