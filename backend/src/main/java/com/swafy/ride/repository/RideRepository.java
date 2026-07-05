package com.swafy.ride.repository;

import com.swafy.common.enums.RideStatus;
import com.swafy.ride.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    List<Ride> findByPassengerIdOrderByRequestedAtDesc(UUID passengerId);

    List<Ride> findByDriverIdOrderByDepartureTimeAsc(UUID driverId);

    List<Ride> findByCorridorIdAndDepartureTimeBetween(
            Long corridorId, Instant from, Instant to);

    List<Ride> findByCorridorIdAndDriverIdAndDepartureTimeBetween(
            Long corridorId, UUID driverId, Instant from, Instant to);

    List<Ride> findByStatusAndDepartureTimeBefore(RideStatus status, Instant now);

    long countByPassengerIdAndStatus(UUID passengerId, RideStatus status);

    List<Ride> findAllByOrderByRequestedAtDesc();

    List<Ride> findByStatusOrderByRequestedAtDesc(RideStatus status);

    List<Ride> findByRequestedAtBetween(Instant from, Instant to);

    long countByStatus(RideStatus status);

    long countByRequestedAtBetween(Instant from, Instant to);
}
