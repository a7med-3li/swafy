package com.vamo.subscription.controller;

import com.vamo.common.dto.ApiResponse;
import com.vamo.subscription.dto.SubscribeRequest;
import com.vamo.subscription.dto.SubscriptionPlanInfo;
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

    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlanInfo>> getPlans() {
        return ResponseEntity.ok(subscriptionService.getAvailablePlans());
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
    @PostMapping("/purchase")
    public ResponseEntity<SubscriptionResponse> purchase(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody SubscribeRequest request) {
        return ResponseEntity.ok(subscriptionService.purchase(UUID.fromString(userId), request));
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'BOTH')")
    @GetMapping("/active")
    public ResponseEntity<SubscriptionResponse> getActive(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(UUID.fromString(userId)));
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
