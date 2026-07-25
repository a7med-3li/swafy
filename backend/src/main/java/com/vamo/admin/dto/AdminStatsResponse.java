package com.vamo.admin.dto;

import java.math.BigDecimal;

public record AdminStatsResponse(
        long totalUsers,
        long totalDrivers,
        long approvedDrivers,
        long pendingDrivers,
        long totalRidesToday,
        long activeSubscriptions,
        long totalSubscriptionsSold,
        BigDecimal totalSubscriptionRevenue,
        long totalRidesBooked,
        long totalRidesInProgress,
        long totalRidesCompleted,
        long totalRidesCancelled,
        long totalRidesNoShow
) {}
