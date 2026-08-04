package com.vamo.admin.service;

import com.vamo.subscription.dto.SubscriptionRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	@Async
	@EventListener
	public void handleSubscriptionRequestedEvent(SubscriptionRequestedEvent event) {
		// Todo: Implement the logic to notify the admin about the subscription request
		// For example, you can send an email or push notification to the admin
		System.out.println("Admin notified about subscription request for passenger: " + event.passenger().getId() +
				" and corridor: " + event.corridor());
	}
}
