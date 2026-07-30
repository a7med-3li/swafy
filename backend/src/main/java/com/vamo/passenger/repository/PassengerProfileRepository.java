package com.vamo.passenger.repository;

import com.vamo.passenger.entity.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, UUID> {
}
