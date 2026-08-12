package com.vamo.notification.service;

import java.util.UUID;
import com.vamo.common.enums.NotificationStatus;
import com.vamo.common.exception.NotificationNotFoundException;
import com.vamo.notification.entity.Notification;
import com.vamo.notification.repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final NotificationRepo notificationRepo;
	
	
	
	public Page<Notification> findAllByReceiverId(UUID receiverId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return notificationRepo.findAllByReceiverIdOrderByUpdatedAtDesc(pageable, receiverId);
	}
	
	// hack: needs to be removed after editing the logic of storing and retrieving
	public Page<Notification> findAll(UUID receiverId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return notificationRepo.findAll(pageable);
	}
	
	public long countUnreadByUserId(UUID receiverId) {
		return notificationRepo.countByReceiverIdAndStatus(receiverId, NotificationStatus.UNREAD);
	}
	
	public void markAsRead(UUID id) {
		Notification notification = notificationRepo.findById(id)
				.orElseThrow(() -> new NotificationNotFoundException(id.toString()));
		notification.markAsRead();
		notificationRepo.save(notification);
	}
	
	
	public Notification save(Notification notification) {
		return notificationRepo.save(notification);
	}
}
