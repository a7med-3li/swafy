package com.vamo.configration.repository;

import java.util.Optional;
import com.vamo.configration.entity.FareConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareConfigRepository extends JpaRepository<FareConfig, Long> {
	
	Optional<FareConfig> findFirstByVehicleTypeAndCityAndActiveTrueOrderByEffectiveFromDesc(
			String vehicleType,
			String cityOrZone
	);
}
