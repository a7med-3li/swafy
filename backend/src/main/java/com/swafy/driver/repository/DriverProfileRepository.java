package com.swafy.driver.repository;

import com.swafy.common.enums.ApprovalStatus;
import com.swafy.driver.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID> {

    Optional<DriverProfile> findByUserId(UUID userId);

    List<DriverProfile> findByApprovalStatus(ApprovalStatus status);

    long countByApprovalStatus(ApprovalStatus status);
}
