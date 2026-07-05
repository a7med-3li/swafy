package com.swafy.driver.dto;

import com.swafy.common.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        TransactionType type,
        BigDecimal amount,
        BigDecimal balanceAfter,
        String reference,
        String description,
        Instant createdAt
) {}
