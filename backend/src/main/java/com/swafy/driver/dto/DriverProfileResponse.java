package com.swafy.driver.dto;

import com.swafy.common.enums.ApprovalStatus;

import java.util.UUID;

public record DriverProfileResponse(
        UUID id,
        String nationalId,
        String licenseNumber,
        boolean onShift,
        Long activeCorridorId,
        ApprovalStatus approvalStatus
) {}
