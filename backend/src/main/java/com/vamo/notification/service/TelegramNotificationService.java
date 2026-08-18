package com.vamo.notification.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService {
	
	private final WebClient webClient = WebClient.builder()
			.baseUrl("https://api.telegram.org")
			.build();
	
	@Value("${telegram.bot.token}")
	private String botToken;
	
	@Value("${telegram.admin-chat-id}")
	private String chatId;
	@Transactional
	public void sendMessage(String text) {
		webClient.post()
				.uri("/bot{token}/sendMessage", botToken)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(Map.of(
						"chat_id", chatId,
						"text", text,
						"parse_mode", "Markdown"
				))
				.retrieve()
				.toBodilessEntity()
				.subscribe();
	}
}
