package com.vamo.notification.template;

public enum NotificationTemplateType {
	
	//todo: refine templates to contain more data
	PASSENGER_REGISTERED(
			"Welcome to Vamo!",
			"Your account has been successfully created.",
			"Welcome {firstName} {lastName}! Your account is ready. You can start booking your rides."
	),
	SUBSCRIPTION_REQUEST_ADMIN(
			"Subscription Request",
			"You have a new subscription request.",
			"You have received a new subscription request from {firstName} {lastName}. " +
					"The passenger wants to subscribe for {CorridorTitle} with {fees} fees. " +
					"You can contact the passenger at {phoneNumber}"
	),
	SUBSCRIPTION_REQUEST(
			"Subscription Request",
			"You have made a new subscription request.",
			"{firstName}, you have registered a new subscription request. " +
					"You want to subscribe for {CorridorTitle} with {fees} fees. " +
					"We will review your request and get back to you soon."
	);
	
	private final String title;
	private final String shortMessage;
	private final String longMessage;
	
	NotificationTemplateType(String title, String shortMessage, String longMessage) {
		this.title = title;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
	}
	
	public NotificationTemplate toTemplate() {
		return new NotificationTemplate(title, shortMessage, longMessage);
	}
}
