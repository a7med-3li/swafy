package com.vamo.notification.service;

import com.vamo.common.dto.SubscriptionRequestDTO;
import com.vamo.common.util.SystemContextService;
import com.vamo.corridor.entity.Corridor;
import com.vamo.notification.handler.NotificationHandler;
import com.vamo.passenger.entity.PassengerProfile;
import com.vamo.subscription.dto.SubscriptionRequestedEvent;
import com.vamo.subscription.entity.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SubscriptionNotificationListenerService {
	
	private final SystemContextService systemContextService;
	private final NotificationService notificationService;
	private final NotificationHandler notificationHandler;
	
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onSubscriptionRequested(SubscriptionRequestedEvent event) {
		
		Subscription subscription = event.subscription();
		PassengerProfile passenger = subscription.getPassenger();
		Corridor corridor = subscription.getCorridor();
		
		SubscriptionRequestDTO requestDTO = SubscriptionRequestDTO.builder()
				.receiverID(systemContextService.getAdminId())
				.firstName(passenger.getUser().getFirstName())
				.lastName(passenger.getUser().getLastName())
				.corridorTitle(corridor.getTitle())
				.fees(corridor.getPrice().toString())
				.phoneNumber(passenger.getUser().getPhoneNumber())
				.build();
		
		notificationService.save(notificationHandler.handleSubscriptionRequest(requestDTO));
		notificationHandler.handleSubscriptionRequestForAdmin(requestDTO);
	}
}
