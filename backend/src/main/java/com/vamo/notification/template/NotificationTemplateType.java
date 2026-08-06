package com.vamo.notification.template;

public enum NotificationTemplateType {
	
	//todo: refine templates to contain more data
	PASSENGER_REGISTERED(
			"Welcome to QuickPay!",
			"Your account has been successfully created.",
			"Welcome {firstName} {lastName}! Your account is ready. You can start sending and receiving money securely."
	),
	
	TRANSACTION_SENT(
			"Money Sent",
			"You sent {amount} EGP to {receiverName}.",
			"Your transfer of {amount} EGP to {receiverName} was completed successfully. " +
					"The transaction reference number is: {transactionId}"
	),
	
	SUBSCRIPTION_REQUEST(
			"Subscription Request",
			"You have a new subscription request.",
			"You have received a new subscription request from {firstName} {lastName}. " +
					"The passenger wants to subscribe for {CorridorTitle} with {fees} fees. " +
					"You can contact the passenger at {phoneNumber}"
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
