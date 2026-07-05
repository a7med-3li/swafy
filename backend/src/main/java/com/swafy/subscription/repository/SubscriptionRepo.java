package com.swafy.subscription.repository;

import com.swafy.common.enums.SubscriptionPlan;
import com.swafy.common.enums.SubscriptionStatus;
import com.swafy.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByPassengerIdAndStatus(UUID passengerId, SubscriptionStatus status);

    List<Subscription> findByPassengerIdOrderByCreatedAtDesc(UUID passengerId);

    List<Subscription> findByStatusAndEndDateBefore(SubscriptionStatus status, LocalDate date);

    List<Subscription> findAllByOrderByCreatedAtDesc();

    List<Subscription> findByPlanOrderByCreatedAtDesc(SubscriptionPlan plan);

    List<Subscription> findByStatusOrderByCreatedAtDesc(SubscriptionStatus status);

    long countByPlan(SubscriptionPlan plan);

    long countByStatus(SubscriptionStatus status);

    @Query("SELECT COALESCE(SUM(s.price), 0) FROM Subscription s")
    BigDecimal totalRevenue();
}
