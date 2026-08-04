package com.vamo.subscription.service;

import com.vamo.common.enums.SubscriptionStatus;
import com.vamo.common.exception.BadRequestException;
import com.vamo.common.exception.NotFoundException;
import com.vamo.corridor.service.CorridorService;
import com.vamo.passenger.entity.PassengerProfile;
import com.vamo.passenger.service.PassengerService;
import com.vamo.payment.entity.Payment;
import com.vamo.subscription.dto.SubscribeRequest;
import com.vamo.subscription.dto.SubscriptionRequestedEvent;
import com.vamo.subscription.dto.SubscriptionResponse;
import com.vamo.subscription.entity.Subscription;
import com.vamo.subscription.repository.SubscriptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepo subscriptionRepo;
    private final PassengerService passengerService;
    private final ApplicationEventPublisher eventPublisher;
    private final CorridorService corridorService;

    // todo: implement admin notification logic on subscription purchase
    
    @Transactional
    public SubscriptionResponse purchase(UUID passengerId, SubscribeRequest request) {
        
        System.out.println(passengerId);
        
        Subscription subscription = Subscription.builder()
                .passenger(passengerService.findById(passengerId))
                .corridor(corridorService.findById(request.corridorID()))
                .status(SubscriptionStatus.PENDING)
                .build();
        
        SubscriptionRequestedEvent requestedEvent =
                new SubscriptionRequestedEvent(subscription.getPassenger(), subscription.getCorridor());
        
        Subscription saved = subscriptionRepo.save(subscription);
        eventPublisher.publishEvent(requestedEvent);
        
        return toResponse(saved);
    }
    
    @Transactional
    public SubscriptionResponse update(Subscription subscription, Payment payment) {
        subscription.setPayment(payment);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        
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

        if (!sub.getPassenger().getId().equals(passengerId)) {
            throw new BadRequestException("Subscription does not belong to this user");
        }
        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException("Subscription is not active");
        }

        sub.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepo.save(sub);
    }

    private SubscriptionResponse toResponse(Subscription sub) {
        return new SubscriptionResponse(
                sub.getId(),
                sub.getCorridor().getPrice(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.getStatus()
        );
    }
}
