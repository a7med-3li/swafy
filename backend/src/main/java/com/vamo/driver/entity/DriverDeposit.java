package com.vamo.driver.entity;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "driver_deposit")
@Getter
@Setter
public class DriverDeposit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id", nullable = false)
	private DriverProfile driver;

	@NotNull(message = "Deposit amount is required")
	@Positive(message = "Deposit amount must be strictly greater than zero")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant receiveDate;

	@Column(nullable = false)
	private boolean isApproved = false;
	
	@Column(nullable = false)
	private boolean isRefunded = false;
	
	@Size(max = 255, message = "Refund reason cannot exceed 255 characters")
	@Column(length = 255)
	private String refundReason;
	
	@PastOrPresent(message = "Refund date cannot be in the future")
	private Instant refundDate;
}
