package com.swafy.driver.service;

import com.swafy.common.enums.TransactionType;
import com.swafy.common.exception.BadRequestException;
import com.swafy.common.exception.NotFoundException;
import com.swafy.driver.dto.TransactionResponse;
import com.swafy.driver.dto.WalletResponse;
import com.swafy.driver.entity.DriverDeposit;
import com.swafy.driver.entity.DriverProfile;
import com.swafy.driver.entity.DriverTransaction;
import com.swafy.driver.repository.DriverDepositRepository;
import com.swafy.driver.repository.DriverProfileRepository;
import com.swafy.driver.repository.DriverTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverWalletService {

    private static final BigDecimal PAYOUT_PER_RIDE = BigDecimal.valueOf(4);
    private static final BigDecimal MIN_DEPOSIT = BigDecimal.valueOf(500);
    private static final BigDecimal MAX_DEPOSIT = BigDecimal.valueOf(1000);

    private final DriverProfileRepository driverProfileRepository;
    private final DriverTransactionRepository transactionRepository;
    private final DriverDepositRepository depositRepository;

    @Transactional
    public void addPayout(UUID driverUserId, UUID rideId) {
        DriverProfile profile = driverProfileRepository.findByUserId(driverUserId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));

        BigDecimal before = profile.getWalletBalance() != null ? profile.getWalletBalance() : BigDecimal.ZERO;
        BigDecimal after = before.add(PAYOUT_PER_RIDE);

        profile.setWalletBalance(after);
        driverProfileRepository.save(profile);

        DriverTransaction tx = DriverTransaction.builder()
                .driverProfileId(profile.getId())
                .type(TransactionType.PAYOUT)
                .amount(PAYOUT_PER_RIDE)
                .balanceBefore(before)
                .balanceAfter(after)
                .reference(rideId.toString())
                .description("Payout for ride confirmation")
                .createdAt(Instant.now())
                .build();
        transactionRepository.save(tx);
    }

    public WalletResponse getWallet(UUID driverUserId) {
        DriverProfile profile = driverProfileRepository.findByUserId(driverUserId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));

        BigDecimal balance = profile.getWalletBalance() != null ? profile.getWalletBalance() : BigDecimal.ZERO;

        int pendingDeposits = depositRepository.findByDriverIdAndIsApprovedFalse(profile.getId()).size();

        return new WalletResponse(balance, pendingDeposits);
    }

    public List<TransactionResponse> getTransactionHistory(UUID driverUserId) {
        DriverProfile profile = driverProfileRepository.findByUserId(driverUserId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));

        return transactionRepository.findByDriverProfileIdOrderByCreatedAtDesc(profile.getId())
                .stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(), tx.getType(), tx.getAmount(),
                        tx.getBalanceAfter(), tx.getReference(),
                        tx.getDescription(), tx.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void addDeposit(UUID driverUserId, BigDecimal amount) {
        if (amount.compareTo(MIN_DEPOSIT) < 0 || amount.compareTo(MAX_DEPOSIT) > 0) {
            throw new BadRequestException("Deposit must be between " + MIN_DEPOSIT + " and " + MAX_DEPOSIT + " EGP");
        }

        DriverProfile profile = driverProfileRepository.findByUserId(driverUserId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));

        DriverDeposit deposit = new DriverDeposit();
        deposit.setDriver(profile);
        deposit.setAmount(amount);
        deposit.setApproved(false);
        deposit.setRefunded(false);
        depositRepository.save(deposit);
    }

    @Transactional
    public void approveDeposit(Long depositId) {
        DriverDeposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new NotFoundException("Deposit not found"));

        if (deposit.isApproved()) {
            throw new BadRequestException("Deposit already approved");
        }

        deposit.setApproved(true);

        DriverProfile profile = deposit.getDriver();
        BigDecimal before = profile.getWalletBalance() != null ? profile.getWalletBalance() : BigDecimal.ZERO;
        BigDecimal after = before.add(deposit.getAmount());

        profile.setWalletBalance(after);
        driverProfileRepository.save(profile);

        DriverTransaction tx = DriverTransaction.builder()
                .driverProfileId(profile.getId())
                .type(TransactionType.DEPOSIT)
                .amount(deposit.getAmount())
                .balanceBefore(before)
                .balanceAfter(after)
                .description("Deposit approved")
                .createdAt(Instant.now())
                .build();
        transactionRepository.save(tx);

        depositRepository.save(deposit);
    }

    @Transactional
    public void forfeitDeposit(Long depositId, String reason) {
        DriverDeposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new NotFoundException("Deposit not found"));

        if (!deposit.isApproved()) {
            throw new BadRequestException("Can only forfeit approved deposits");
        }
        if (deposit.isRefunded()) {
            throw new BadRequestException("Deposit already refunded");
        }

        DriverProfile profile = deposit.getDriver();
        BigDecimal before = profile.getWalletBalance() != null ? profile.getWalletBalance() : BigDecimal.ZERO;
        BigDecimal after = before.subtract(deposit.getAmount());

        if (after.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Insufficient balance to forfeit deposit");
        }

        profile.setWalletBalance(after);
        driverProfileRepository.save(profile);

        deposit.setRefunded(true);
        deposit.setRefundReason(reason);
        deposit.setRefundDate(Instant.now());
        depositRepository.save(deposit);

        DriverTransaction tx = DriverTransaction.builder()
                .driverProfileId(profile.getId())
                .type(TransactionType.FORFEIT)
                .amount(deposit.getAmount().negate())
                .balanceBefore(before)
                .balanceAfter(after)
                .description("Deposit forfeited: " + reason)
                .createdAt(Instant.now())
                .build();
        transactionRepository.save(tx);
    }
}
