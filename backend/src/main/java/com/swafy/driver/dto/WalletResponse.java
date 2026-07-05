package com.swafy.driver.dto;

import java.math.BigDecimal;

public record WalletResponse(
        BigDecimal balance,
        int pendingDepositAmount
) {}
