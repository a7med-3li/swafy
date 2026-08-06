package com.vamo.notification.entity;

import java.time.LocalDate;
import java.util.UUID;
import com.vamo.common.enums.NotificationStatus;
import com.vamo.common.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="notifications")
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@NotNull
	private UUID receiverId;
	
	@NotNull
	private String receiverName;
	
	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationType type;
	
	@NotNull
	@Column(nullable = false)
	private String title;
	
	@NotNull
	@Column(nullable = false)
	private String shortMessage;
	
	@NotNull
	@Column(nullable = false)
	private String message;
	
	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private NotificationStatus status = NotificationStatus.UNREAD;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "TEXT")
	private String metadata;
	
	@Column(nullable = false)
	private LocalDate createdAt;
	
	@Column(nullable = false)
	private LocalDate updatedAt;
	
	private LocalDate readAt;
	
	public void markAsRead() {
		this.status = NotificationStatus.READ;
		this.readAt = LocalDate.now();
	}
	
	@PrePersist
	public void prePersist() {
		LocalDate now = LocalDate.now();
		this.createdAt = now;
		this.updatedAt = now;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDate.now();
	}
}
