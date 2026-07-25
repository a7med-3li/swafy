package com.vamo.passenger.service;

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
	
	@TransactionalEventListener
	public void onUserRegistered(PassengerRegisteredEvent event) {
		PassengerProfile profile = new PassengerProfile(
				null, // id will be generated
				event.user(),
				null, // homeStopId
				0,    // rideBalance
				null, // subExpires
				0     // noShowCount
		);
		passengerProfileRepository.save(profile);
	}
}
