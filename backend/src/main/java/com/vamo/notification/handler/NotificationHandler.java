package com.vamo.notification.handler;

import java.util.Map;
import com.vamo.common.dto.SubscriptionRequestDTO;
import com.vamo.common.enums.NotificationStatus;
import com.vamo.common.enums.NotificationType;
import com.vamo.common.events.PassengerRegisteredEvent;
import com.vamo.common.util.SystemContextService;
import com.vamo.notification.entity.Notification;
import com.vamo.notification.service.NotificationService;
import com.vamo.notification.service.NotificationTemplateService;
import com.vamo.notification.service.TelegramNotificationService;
import com.vamo.notification.template.NotificationTemplateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHandler {
	private final NotificationTemplateService templateService;
	private final SystemContextService systemContextService;
	private final NotificationService notificationService;
	private final TelegramNotificationService telegramNotificationService;
	
	public Notification handlePassengerRegistered(PassengerRegisteredEvent userCreatedEvent) {
		
		var template = templateService.build(
				NotificationTemplateType.PASSENGER_REGISTERED,
				Map.of(
						"firstName", userCreatedEvent.user().getFirstName(),
						"lastName", userCreatedEvent.user().getLastName()
				)
		);
		
		return Notification.builder()
				.receiverId(userCreatedEvent.user().getId())
				.receiverName(userCreatedEvent.user().getFirstName() + " " + userCreatedEvent.user().getLastName())
				.type(NotificationType.PASSENGER_REGISTERED)
				.title(template.getTitle())
				.shortMessage(template.getShortMessage())
				.message(template.getLongMessage())
				.status(NotificationStatus.UNREAD)
				.build();
	}
	
	public Notification handleSubscriptionRequest(SubscriptionRequestDTO subscriptionRequestDTO) {
		var template = templateService.build(
				NotificationTemplateType.SUBSCRIPTION_REQUEST,
				Map.of(
						"firstName", subscriptionRequestDTO.firstName(),
						"CorridorTitle", subscriptionRequestDTO.corridorTitle(),
						"fees", subscriptionRequestDTO.fees()
				)
		);
		
		return Notification.builder()
				.receiverId(subscriptionRequestDTO.receiverID())
				.receiverName(subscriptionRequestDTO.firstName() + " " + subscriptionRequestDTO.lastName())
				.type(NotificationType.SUBSCRIPTION_REQUEST)
				.title(template.getTitle())
				.shortMessage(template.getShortMessage())
				.message(template.getLongMessage())
				.status(NotificationStatus.UNREAD)
				.build();
	}
	
	public void handleSubscriptionRequestForAdmin(SubscriptionRequestDTO subRequestDTO) {
		
		var template = templateService.build(
				NotificationTemplateType.SUBSCRIPTION_REQUEST_ADMIN,
				Map.of(
						"firstName", subRequestDTO.firstName(),
						"lastName", subRequestDTO.lastName(),
						"CorridorTitle", subRequestDTO.corridorTitle(),
						"fees", subRequestDTO.fees(),
						"phoneNumber", subRequestDTO.phoneNumber()
				)
		);
		
		Notification notification = notificationService.save(Notification.builder()
				.receiverId(systemContextService.getAdminId())
				.receiverName(subRequestDTO.firstName() + " " + subRequestDTO.lastName())
				.type(NotificationType.SUBSCRIPTION_REQUEST)
				.title(template.getTitle())
				.shortMessage(template.getShortMessage())
				.message(template.getLongMessage())
				.status(NotificationStatus.UNREAD)
				.build());
		
		telegramNotificationService.sendMessage(notification.getMessage());
	}
	
}
