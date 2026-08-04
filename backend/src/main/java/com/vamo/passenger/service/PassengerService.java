package com.vamo.passenger.service;

import java.util.List;
import java.util.UUID;
import com.vamo.common.events.PassengerRegisteredEvent;
import com.vamo.passenger.entity.PassengerProfile;
import com.vamo.passenger.repository.PassengerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class PassengerService {
	// TODO: implement passenger-related logic
	
	private final PassengerProfileRepository passengerProfileRepository;
	
	public PassengerProfile findById(UUID id) {
		return passengerProfileRepository.findByUserId(id)
				.orElseThrow(() -> new RuntimeException("Passenger not found"));
	}
	
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onPassengerRegisteredEvent(PassengerRegisteredEvent event) {
		System.out.println("Passenger registered event received");
		createPassengerProfile(new PassengerProfile(event.user().getId(), event.user(), List.of()));
	}
	
	private void createPassengerProfile(PassengerProfile passengerProfile) {
		passengerProfileRepository.save(passengerProfile);
	}
}
