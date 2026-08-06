package com.vamo.notification.service;

import java.util.Map;
import com.vamo.notification.template.NotificationTemplate;
import com.vamo.notification.template.NotificationTemplateType;
import com.vamo.notification.template.TemplateEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {
	
	private final TemplateEngine templateEngine;
	
	
	public NotificationTemplate build(
			NotificationTemplateType type,
			Map<String, String> variables
	) {
		NotificationTemplate template = type.toTemplate();
		return templateEngine.process(template, variables);
	}
}
