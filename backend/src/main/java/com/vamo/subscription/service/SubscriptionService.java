package com.vamo.subscription.service;

import com.vamo.common.enums.SubscriptionStatus;
import com.vamo.common.exception.BadRequestException;
import com.vamo.common.exception.NotFoundException;
import com.vamo.corridor.entity.Corridor;
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepo subscriptionRepo;
    private final PassengerService passengerService;
    private final ApplicationEventPublisher eventPublisher;
    private final CorridorService corridorService;

    @Transactional
    public SubscriptionResponse purchase(UUID passengerId, SubscribeRequest request) {
        
        PassengerProfile passenger = passengerService.findById(passengerId);
        Corridor corridor = corridorService.findById(request.corridorID());
        
        Subscription subscription = Subscription.builder()
                .passenger(passenger)
                .corridor(corridor)
                .status(SubscriptionStatus.PENDING)
                .build();
        
        validateSubscription(subscription);
    
        Subscription saved = subscriptionRepo.save(subscription);
        
        eventPublisher.publishEvent(new SubscriptionRequestedEvent(saved.getId()));
        
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
    
    private void validateSubscription(Subscription subscription) {
        
        UUID passengerId = subscription.getPassenger().getId();
        Long corridorId = subscription.getCorridor().getId();
        List<SubscriptionStatus> blockingStatuses = List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PENDING);
        
        subscriptionRepo.findFirstByPassengerIdAndCorridorIdAndStatusIn(passengerId, corridorId, blockingStatuses)
                .ifPresent(existingSub -> {
                    if (existingSub.getStatus() == SubscriptionStatus.ACTIVE) {
                        throw new BadRequestException("User already has an active subscription for this corridor");
                    }
                    throw new BadRequestException("User already has a pending subscription for this corridor");
                });
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
