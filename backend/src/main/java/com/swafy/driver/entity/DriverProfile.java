package com.swafy.driver.entity;

import com.swafy.common.enums.ApprovalStatus;
import com.swafy.corridor.entity.Corridor;
import com.swafy.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "driver_profiles")
public class DriverProfile {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    private String nationalId;
    private String licenseNumber;
    private BigDecimal depositAmount;
    private BigDecimal walletBalance;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_corridor_id")
    private Corridor activeCorridor;    // null when off shift
    
    private boolean isOnShift;
    
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;  // PENDING, APPROVED, REJECTED
}
