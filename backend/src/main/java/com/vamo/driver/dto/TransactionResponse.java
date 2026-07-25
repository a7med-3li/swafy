package com.vamo.driver.dto;

import com.vamo.common.enums.TransactionType;

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
