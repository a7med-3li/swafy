package com.vamo.admin.service;

import com.vamo.notification.service.TelegramNotificationService;
import com.vamo.subscription.dto.SubscriptionRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final TelegramNotificationService telegramNotificationService;
	
	@Async
	@EventListener
	public void handleSubscriptionRequestedEvent(SubscriptionRequestedEvent event) {
		// Todo: Implement the logic to notify the admin about the subscription request
		// For example, you can send an email or push notification to the admin
		
		String message = String.format(
				"🔔 *New Subscription Request*\n\n" +
						"*Student:* %s\n" +
						"*Corridor:* %s\n" +
						"*Price:* %s\n" +
						"*Phone Number:* %s",
				event.passenger().getUser().getFirstName(), event.corridor().getTitle(), event.corridor().getPrice(),
				event.passenger().getUser().getPhoneNumber());
		
		telegramNotificationService.sendMessage(message);
	}
}
