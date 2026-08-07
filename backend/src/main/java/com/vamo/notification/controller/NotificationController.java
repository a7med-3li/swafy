package com.vamo.notification.controller;

import com.vamo.auth.security.JwtAuthentication;
import com.vamo.notification.dto.NotificationResponseDto;
import com.vamo.notification.entity.Notification;
import com.vamo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        UUID receiverId = UUID.fromString(jwtAuthentication.getUserId());

        List<Notification> notifications = notificationService.findAllByReceiverId(receiverId, page, size)
                .getContent();

        return ResponseEntity.ok(
                notifications.stream()
                        .map(NotificationResponseDto::from)
                        .toList()
        );
    }
}
