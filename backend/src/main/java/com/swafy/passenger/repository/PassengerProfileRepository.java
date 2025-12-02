package com.swafy.passenger.repository;

import com.swafy.passenger.entity.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {
}
