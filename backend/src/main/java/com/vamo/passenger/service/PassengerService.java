package com.vamo.passenger.service;

import java.util.List;
import java.util.UUID;
import com.vamo.common.events.PassengerRegisteredEvent;
import com.vamo.passenger.entity.PassengerProfile;
import com.vamo.passenger.repository.PassengerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class PassengerService {
	// TODO: implement passenger-related logic
	
	private final PassengerProfileRepository passengerProfileRepository;
	
	public PassengerProfile findById(UUID id) {
		return passengerProfileRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Passenger not found"));
	}

	public void createPassengerProfile(PassengerProfile passengerProfile) {
		passengerProfileRepository.save(passengerProfile);
	}
}
