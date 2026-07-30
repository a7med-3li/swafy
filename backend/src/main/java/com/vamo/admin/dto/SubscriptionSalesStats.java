package com.vamo.admin.dto;

import java.math.BigDecimal;

public record SubscriptionSalesStats(
        long totalActive,
        long totalExpired,
        long totalCancelled,
        long totalSuspended
        //long studentBasicCount,
       // long studentPlusCount,
        //long corporateCommuterCount,
        //BigDecimal totalRevenue
) {}
