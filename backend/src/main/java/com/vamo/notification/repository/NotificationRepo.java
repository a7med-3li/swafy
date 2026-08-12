package com.vamo.notification.repository;

import java.util.UUID;
import com.vamo.common.enums.NotificationStatus;
import com.vamo.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepo extends JpaRepository<Notification, UUID> {
	
	Page<Notification> findAllByReceiverIdOrderByUpdatedAtDesc(Pageable pageable, UUID receiverId);
	
	long countByReceiverIdAndStatus(UUID receiverId, NotificationStatus status);
	
}
