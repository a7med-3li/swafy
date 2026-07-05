package com.swafy.admin.dto;

import com.swafy.common.enums.ApprovalStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record DriverAdminItem(
        UUID profileId,
        UUID userId,
        String name,
        String phoneNumber,
        String nationalId,
        String licenseNumber,
        boolean onShift,
        String activeCorridor,
        BigDecimal walletBalance,
        ApprovalStatus approvalStatus
) {}
