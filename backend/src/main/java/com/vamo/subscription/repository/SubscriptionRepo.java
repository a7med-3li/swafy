package com.vamo.subscription.repository;

import com.vamo.common.enums.SubscriptionPlan;
import com.vamo.common.enums.SubscriptionStatus;
import com.vamo.subscription.entity.Subscription;
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
    
    Optional<Subscription> findFirstByPassengerIdAndCorridorIdAndStatusIn(
            UUID passengerId,
            Long corridorId, // Or UUID, depending on your Corridor ID type
            List<SubscriptionStatus> statuses
    );
    
    List<Subscription> findByPassengerIdOrderByCreatedAtDesc(UUID passengerId);

    List<Subscription> findByStatusAndEndDateBefore(SubscriptionStatus status, LocalDate date);

    List<Subscription> findByStatusOrderByCreatedAtDesc(SubscriptionStatus status);

    long countByStatus(SubscriptionStatus status);

//    @Query("SELECT COALESCE(SUM(s.price), 0) FROM Subscription s")
//    BigDecimal totalRevenue();
}
