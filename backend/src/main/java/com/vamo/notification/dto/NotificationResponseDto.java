package com.vamo.notification.dto;

import com.vamo.notification.entity.Notification;

public record NotificationResponseDto(
    String id,
    String title,
    String shortDescription,
    String message,
    String status,
    String createdAt
) {
    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
            notification.getId().toString(),
            notification.getTitle(),
            notification.getShortMessage(),
            notification.getMessage(),
            notification.getStatus().name(),
            notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null
        );
    }
}
