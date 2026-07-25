package com.vamo.driver.repository;

import com.vamo.driver.entity.DriverDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DriverDepositRepository extends JpaRepository<DriverDeposit, Long> {

    List<DriverDeposit> findByDriverIdAndIsApprovedFalse(UUID driverProfileId);

    List<DriverDeposit> findAllByOrderByReceiveDateDesc();

    List<DriverDeposit> findByIsApprovedOrderByReceiveDateDesc(boolean isApproved);
}
