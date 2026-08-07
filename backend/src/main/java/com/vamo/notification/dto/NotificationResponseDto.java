package com.vamo.notification.dto;

import com.vamo.notification.entity.Notification;

public record NotificationResponseDto(
    Integer id,
    String title,
    String shortDescription,
    String message
) {
    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
            Math.abs(notification.getId().hashCode()),
            notification.getTitle(),
            notification.getShortMessage(),
            notification.getMessage()
        );
    }
}
