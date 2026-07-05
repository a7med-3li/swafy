package com.swafy.driver.repository;

import com.swafy.driver.entity.DriverTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DriverTransactionRepository extends JpaRepository<DriverTransaction, Long> {

    List<DriverTransaction> findByDriverProfileIdOrderByCreatedAtDesc(UUID driverProfileId);
}
