package com.vamo.driver.dto;

import com.vamo.common.enums.ApprovalStatus;

import java.util.UUID;

public record DriverProfileResponse(
        UUID id,
        String nationalId,
        String licenseNumber,
        boolean onShift,
        Long activeCorridorId,
        ApprovalStatus approvalStatus
) {}
