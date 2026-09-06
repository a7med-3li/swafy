package com.vamo.configration.service;

import com.vamo.common.enums.VehicleType;
import com.vamo.configration.dto.FareConfigResponse;
import com.vamo.configration.dto.SaveFareConfigRequest;
import com.vamo.configration.entity.FareConfig;
import com.vamo.configration.repository.FareConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FareConfigService {

	private final FareConfigRepository repository;

	@Transactional(readOnly = true)
	public List<FareConfigResponse> getAll() {
		return repository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional
	public FareConfigResponse create(SaveFareConfigRequest request) {
		FareConfig config = new FareConfig();
		config.setVehicleType(request.vehicleType().name());
		config.setCity(request.city());
		config.setBaseFare(request.baseFare());
		config.setPerKmRate(request.perKmRate());
		config.setPerMinuteRate(request.perMinuteRate());
		config.setMinimumFare(request.minimumFare());
		config.setWaitingRatePerMinute(request.waitingRatePerMinute());
		config.setSurgeMultiplier(request.surgeMultiplier() == null
				? BigDecimal.ONE
				: request.surgeMultiplier());
		config.setActive(request.active());
		config.setEffectiveFrom(request.effectiveFrom());
		config.setCreatedAt(LocalDateTime.now());
		return toResponse(repository.save(config));
	}

	private FareConfigResponse toResponse(FareConfig c) {
		VehicleType type;
		try {
			type = VehicleType.valueOf(c.getVehicleType().toUpperCase());
		} catch (Exception e) {
			type = null;
		}

		return new FareConfigResponse(
				c.getId(),
				type,
				c.getCity(),
				c.getBaseFare(),
				c.getPerKmRate(),
				c.getPerMinuteRate(),
				c.getMinimumFare(),
				c.getWaitingRatePerMinute(),
				c.getSurgeMultiplier(),
				c.isActive(),
				c.getEffectiveFrom(),
				c.getCreatedAt()
		);
	}
}