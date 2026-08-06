package com.vamo.notification.template;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TemplateEngine {
	
	public NotificationTemplate process(
			NotificationTemplate template,
			Map<String, String> variables
	) {
		String title = replace(template.getTitle(), variables);
		String shortMsg = replace(template.getShortMessage(), variables);
		String longMsg = replace(template.getLongMessage(), variables);
		
		return NotificationTemplate.builder()
				.title(title)
				.shortMessage(shortMsg)
				.longMessage(longMsg)
				.build();
	}
	
	private String replace(String text, Map<String, String> variables) {
		if (text == null) return null;
		
		for (Map.Entry<String, String> entry : variables.entrySet()) {
			text = text.replace("{" + entry.getKey() + "}", entry.getValue());
		}
		return text;
	}
}
