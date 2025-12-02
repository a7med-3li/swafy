package com.swafy.driver.repository;

import com.swafy.driver.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {
}
