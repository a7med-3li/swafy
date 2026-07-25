package com.vamo.subscription.service;

import com.vamo.common.enums.SubscriptionPlan;
import com.vamo.common.enums.SubscriptionStatus;
import com.vamo.common.exception.BadRequestException;
import com.vamo.common.exception.NotFoundException;
import com.vamo.subscription.dto.SubscribeRequest;
import com.vamo.subscription.dto.SubscriptionPlanInfo;
import com.vamo.subscription.dto.SubscriptionResponse;
import com.vamo.subscription.entity.Subscription;
import com.vamo.subscription.repository.SubscriptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepo subscriptionRepo;

    private static final Map<SubscriptionPlan, PlanConfig> PLANS = Map.of(
            SubscriptionPlan.STUDENT_BASIC, new PlanConfig("Student Basic", "40 rides per month", 200, 40),
            SubscriptionPlan.STUDENT_PLUS, new PlanConfig("Student Plus", "70 rides per month", 300, 70),
            SubscriptionPlan.CORPORATE_COMMUTER, new PlanConfig("Corporate Commuter", "60 rides per month", 350, 60)
    );

    private record PlanConfig(String displayName, String description, int priceEgp, int totalRides) {}

    public List<SubscriptionPlanInfo> getAvailablePlans() {
        return PLANS.entrySet().stream()
                .map(e -> {
                    PlanConfig cfg = e.getValue();
                    BigDecimal price = BigDecimal.valueOf(cfg.priceEgp());
                    BigDecimal perRide = price.divide(BigDecimal.valueOf(cfg.totalRides()), 2, RoundingMode.HALF_UP);
                    return new SubscriptionPlanInfo(e.getKey(), cfg.displayName(), cfg.description(),
                            price, cfg.totalRides(), perRide);
                })
                .toList();
    }

    @Transactional
    public SubscriptionResponse purchase(UUID passengerId, SubscribeRequest request) {
        PlanConfig cfg = PLANS.get(request.plan());
        if (cfg == null) {
            throw new BadRequestException("Invalid subscription plan: " + request.plan());
        }

        subscriptionRepo.findByPassengerIdAndStatus(passengerId, SubscriptionStatus.ACTIVE)
                .ifPresent(s -> {
                    throw new BadRequestException("User already has an active subscription");
                });

        LocalDate now = LocalDate.now();
        Subscription subscription = Subscription.builder()
                .passengerId(passengerId)
                .plan(request.plan())
                .totalRides(cfg.totalRides())
                .remainingRides(cfg.totalRides())
                .price(BigDecimal.valueOf(cfg.priceEgp()))
                .startDate(now)
                .endDate(now.plusMonths(1))
                .status(SubscriptionStatus.ACTIVE)
                .paymentMethod(request.paymentMethod())
                .paymentReference(request.paymentReference())
                .createdAt(Instant.now())
                .autoRenew(false)
                .build();

        Subscription saved = subscriptionRepo.save(subscription);
        return toResponse(saved);
    }

    public SubscriptionResponse getActiveSubscription(UUID passengerId) {
        return subscriptionRepo.findByPassengerIdAndStatus(passengerId, SubscriptionStatus.ACTIVE)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("No active subscription found"));
    }

    public List<SubscriptionResponse> getSubscriptionHistory(UUID passengerId) {
        return subscriptionRepo.findByPassengerIdOrderByCreatedAtDesc(passengerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void cancelSubscription(Long subscriptionId, UUID passengerId) {
        Subscription sub = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException("Subscription not found"));

        if (!sub.getPassengerId().equals(passengerId)) {
            throw new BadRequestException("Subscription does not belong to this user");
        }
        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException("Subscription is not active");
        }

        sub.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepo.save(sub);
    }

    @Transactional
    public void deductRide(Long subscriptionId) {
        Subscription sub = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException("Subscription not found"));

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException("Subscription is not active");
        }
        if (sub.getRemainingRides() <= 0) {
            throw new BadRequestException("No remaining rides on this subscription");
        }

        sub.setRemainingRides(sub.getRemainingRides() - 1);
        subscriptionRepo.save(sub);
    }

    @Transactional
    public void expireSubscriptions() {
        List<Subscription> expired = subscriptionRepo.findByStatusAndEndDateBefore(
                SubscriptionStatus.ACTIVE, LocalDate.now());
        for (Subscription sub : expired) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepo.save(sub);
        }
    }

    private SubscriptionResponse toResponse(Subscription sub) {
        return new SubscriptionResponse(
                sub.getId(),
                sub.getPlan(),
                sub.getTotalRides(),
                sub.getRemainingRides(),
                sub.getPrice(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.getStatus(),
                sub.isAutoRenew()
        );
    }
}
