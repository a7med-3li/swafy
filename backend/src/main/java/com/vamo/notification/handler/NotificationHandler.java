package com.vamo.notification.handler;

import java.util.Map;
import com.vamo.common.dto.SubscriptionRequestDTO;
import com.vamo.common.enums.NotificationStatus;
import com.vamo.common.enums.NotificationType;
import com.vamo.common.events.PassengerRegisteredEvent;
import com.vamo.notification.entity.Notification;
import com.vamo.notification.service.NotificationTemplateService;
import com.vamo.notification.template.NotificationTemplateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHandler {
	private final NotificationTemplateService templateService;
	
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
	
	public Notification handleSubscriptionRequest(SubscriptionRequestDTO subRequestDTO) {
		
		var template = templateService.build(
				NotificationTemplateType.SUBSCRIPTION_REQUEST,
				Map.of(
						"firstName", subRequestDTO.firstName(),
						"lastName", subRequestDTO.lastName(),
						"CorridorTitle", subRequestDTO.corridorTitle(),
						"fees", subRequestDTO.fees(),
						"phoneNumber", subRequestDTO.phoneNumber()
				)
		);
		
		return Notification.builder()
				.receiverId(subRequestDTO.receiverID())
				.receiverName(subRequestDTO.firstName() + " " + subRequestDTO.lastName())
				.type(NotificationType.SUBSCRIPTION_REQUEST)
				.title(template.getTitle())
				.shortMessage(template.getShortMessage())
				.message(template.getLongMessage())
				.status(NotificationStatus.UNREAD)
				.build();
	}
	
}
