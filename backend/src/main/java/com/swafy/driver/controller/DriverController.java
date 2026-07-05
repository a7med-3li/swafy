package com.swafy.driver.controller;

import com.swafy.driver.dto.ActivateCorridorRequest;
import com.swafy.driver.dto.DriverProfileResponse;
import com.swafy.driver.dto.TransactionResponse;
import com.swafy.driver.dto.WalletResponse;
import com.swafy.driver.service.DriverService;
import com.swafy.driver.service.DriverWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;
    private final DriverWalletService walletService;

    @PreAuthorize("hasAnyRole('DRIVER', 'BOTH')")
    @GetMapping("/profile")
    public ResponseEntity<DriverProfileResponse> getProfile(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(driverService.getProfile(UUID.fromString(userId)));
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'BOTH')")
    @PostMapping("/activate-corridor")
    public ResponseEntity<?> activateCorridor(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody ActivateCorridorRequest request) {
        driverService.activateCorridor(UUID.fromString(userId), request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'BOTH')")
    @PostMapping("/toggle-shift")
    public ResponseEntity<?> toggleShift(
            @AuthenticationPrincipal String userId,
            @RequestParam boolean onShift) {
        driverService.toggleShift(UUID.fromString(userId), onShift);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'BOTH')")
    @GetMapping("/wallet")
    public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(walletService.getWallet(UUID.fromString(userId)));
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'BOTH')")
    @GetMapping("/wallet/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(UUID.fromString(userId)));
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'BOTH')")
    @PostMapping("/wallet/deposit")
    public ResponseEntity<?> requestDeposit(
            @AuthenticationPrincipal String userId,
            @RequestParam BigDecimal amount) {
        walletService.addDeposit(UUID.fromString(userId), amount);
        return ResponseEntity.ok().build();
    }
}
