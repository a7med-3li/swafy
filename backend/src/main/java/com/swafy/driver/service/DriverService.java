package com.swafy.driver.service;

import com.swafy.common.enums.ApprovalStatus;
import com.swafy.common.events.DriverRegisteredEvent;
import com.swafy.driver.entity.DriverProfile;
import com.swafy.driver.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverService {
    // TODO: implement driver business logic
	
	private final DriverProfileRepository driverProfileRepository;
	
	@TransactionalEventListener
	public void onDriverRegistered(DriverRegisteredEvent event) {
		DriverProfile profile = new DriverProfile(null,
				event.user(),
				event.registerDriverRequest().nationalId(),
				event.registerDriverRequest().licenseNumber(),
				null,
				null,
				null,
				false,
				ApprovalStatus.PENDING
		);
		driverProfileRepository.save(profile);
	}
}
