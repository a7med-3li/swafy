package com.swafy.admin.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DepositItem(
        Long id,
        UUID driverProfileId,
        BigDecimal amount,
        Instant receiveDate,
        boolean isApproved,
        boolean isRefunded,
        String refundReason,
        Instant refundDate
) {}
