package com.vamo.admin.controller;

import com.vamo.admin.dto.*;
import com.vamo.admin.service.AdminService;
import com.vamo.admin.service.AdminStatsService;
import com.vamo.common.enums.ApprovalStatus;
import com.vamo.common.enums.RideStatus;
import com.vamo.common.enums.SubscriptionPlan;
import com.vamo.common.enums.SubscriptionStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminStatsService.getStats());
    }

    @GetMapping("/rides")
    public ResponseEntity<List<RideAdminItem>> getRides(
            @RequestParam(required = false) RideStatus status,
            @RequestParam(required = false) UUID driverId,
            @RequestParam(required = false) UUID passengerId) {
        return ResponseEntity.ok(adminStatsService.getRides(status, driverId, passengerId));
    }

    @GetMapping("/rides/{id}")
    public ResponseEntity<RideAdminItem> getRideDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(adminStatsService.getRideDetail(id));
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<DriverAdminItem>> getDrivers(
            @RequestParam(required = false) ApprovalStatus approvalStatus) {
        return ResponseEntity.ok(adminStatsService.getDrivers(approvalStatus));
    }

    @PostMapping("/drivers/{id}/approve")
    public ResponseEntity<Void> approveDriver(@PathVariable UUID id) {
        adminStatsService.approveDriver(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/drivers/{id}/reject")
    public ResponseEntity<Void> rejectDriver(@PathVariable UUID id) {
        adminStatsService.rejectDriver(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/deposits")
    public ResponseEntity<List<DepositItem>> getDeposits(
            @RequestParam(required = false) Boolean approved) {
        return ResponseEntity.ok(adminStatsService.getDeposits(approved));
    }

    @PostMapping("/deposits/{id}/approve")
    public ResponseEntity<Void> approveDeposit(@PathVariable Long id) {
        adminStatsService.approveDeposit(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposits/{id}/forfeit")
    public ResponseEntity<Void> forfeitDeposit(
            @PathVariable Long id,
            @Valid @RequestBody ForfeitDepositRequest request) {
        adminStatsService.forfeitDeposit(id, request.reason());
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/subscriptions")
//    public ResponseEntity<List<SubscriptionSalesItem>> getSubscriptions(
//            @RequestParam(required = false) SubscriptionPlan plan,
//            @RequestParam(required = false) SubscriptionStatus status) {
//        return ResponseEntity.ok(adminService.getSubscriptions(plan, status));
//    }

    @GetMapping("/subscriptions/stats")
    public ResponseEntity<SubscriptionSalesStats> getSubscriptionStats() {
        return ResponseEntity.ok(adminStatsService.getSubscriptionStats());
    }

    @GetMapping("/subscriptions/pending")
    public ResponseEntity<List<PendingSubscriptionItem>> getPendingSubscriptions() {
        return ResponseEntity.ok(adminStatsService.getPendingSubscriptions());
    }

    @GetMapping("/no-shows/rides")
    public ResponseEntity<List<RideAdminItem>> getNoShowRides() {
        return ResponseEntity.ok(adminStatsService.getNoShowRides());
    }
    
}
