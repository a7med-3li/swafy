package com.vamo.pricing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.vamo.configration.entity.FareConfig;
import com.vamo.configration.repository.FareConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FareCalculationService {
	
	private final FareConfigRepository fareConfigRepo;
	
	public BigDecimal calculateFare(
			String vehicleType,
			double distanceKm,
			double durationMinutes) {
		
		FareConfig config = fareConfigRepo
				.findFirstByVehicleTypeAndCityAndActiveTrueOrderByEffectiveFromDesc(vehicleType, "BENI_SUEF")
				.orElseThrow(() -> new IllegalStateException("No active fare config for " + vehicleType + "/" + "BENI_SUEF"));
		
		BigDecimal distanceCost = config.getPerKmRate().multiply(BigDecimal.valueOf(distanceKm));
		BigDecimal timeCost = config.getPerMinuteRate().multiply(BigDecimal.valueOf(durationMinutes));
	
		BigDecimal subtotal = config.getBaseFare()
				.add(distanceCost)
				.add(timeCost)
				.multiply(config.getSurgeMultiplier());
		
		return subtotal.max(config.getMinimumFare())
				.setScale(2, RoundingMode.HALF_UP);
	}
}
