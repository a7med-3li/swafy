package com.swafy.corridor.controller;

import java.util.List;
import com.swafy.corridor.dto.CorridorResponse;
import com.swafy.corridor.dto.SaveCorridorRequest;
import com.swafy.corridor.dto.StopResponse;
import com.swafy.corridor.entity.Corridor;
import com.swafy.corridor.service.CorridorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corridors")
public class CorridorController {
	
	private final CorridorService corridorService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<?> createCorridor(@Valid @RequestBody SaveCorridorRequest request) {
		Corridor savedCorridor = corridorService.createCorridor(request);
		
		// Map the entity to the DTO before returning
		List<StopResponse> stopDTOs = savedCorridor.getStops().stream()
				.map(vbs -> new StopResponse(vbs.getId(), vbs.getName(), vbs.getLatitude(), vbs.getLongitude()))
				.toList();
		
		CorridorResponse response = new CorridorResponse(
				savedCorridor.getId(),
				savedCorridor.getName(),
				savedCorridor.getPrice(),
				stopDTOs
		);
		return ResponseEntity.ok(response);
	}
}
