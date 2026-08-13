package com.vamo.subscription.controller;

import com.vamo.common.annotation.CurrentPassengerId;
import com.vamo.common.dto.ApiResponse;
import com.vamo.subscription.dto.SubscribeRequest;
import com.vamo.subscription.dto.SubscriptionResponse;
import com.vamo.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    @PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
    @PostMapping("/purchase")
    public ResponseEntity<SubscriptionResponse> purchase(
            @AuthenticationPrincipal String passengerId,
            @Valid @RequestBody SubscribeRequest request) {
        System.out.println("Passenger ID: " + passengerId);
        return ResponseEntity.ok(subscriptionService.purchase(UUID.fromString(passengerId), request));
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
    @GetMapping("/active")
    public ResponseEntity<SubscriptionResponse> getActive(@CurrentPassengerId UUID userId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(userId));
    }
	
	@PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
	@GetMapping("/pending")
	public ResponseEntity<SubscriptionResponse> getPending(@CurrentPassengerId UUID userId) {
		return ResponseEntity.ok(subscriptionService.getPendingSubscription(userId));
	}

    @PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
    @GetMapping("/history")
    public ResponseEntity<List<SubscriptionResponse>> getHistory(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionHistory(UUID.fromString(userId)));
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancel(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id) {
        subscriptionService.cancelSubscription(id, UUID.fromString(userId));
        return ResponseEntity.ok(new ApiResponse(true, "Subscription cancelled"));
    }
}
