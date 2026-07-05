package com.swafy.passenger.repository;

import com.swafy.passenger.entity.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, UUID> {

    List<PassengerProfile> findAllByOrderByNoShowCountDesc();
}
