package com.vamo.corridor.controller;

import java.util.List;
import com.vamo.corridor.dto.CorridorResponse;
import com.vamo.corridor.dto.SaveCorridorRequest;
import com.vamo.corridor.dto.StopResponse;
import com.vamo.corridor.entity.Corridor;
import com.vamo.corridor.service.CorridorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
	@GetMapping
	public ResponseEntity<List<CorridorResponse>> getAllCorridors() {
		List<Corridor> corridors = corridorService.getAllCorridors();
		List<CorridorResponse> response = corridors.stream()
				.map(c -> new CorridorResponse(
						c.getId(),
						c.getName(),
						c.getPrice(),
						c.getStops().stream()
								.map(vbs -> new StopResponse(vbs.getId(), vbs.getName(), vbs.getLatitude(), vbs.getLongitude()))
								.toList()
				))
				.toList();
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<CorridorResponse> createCorridor(@Valid @RequestBody SaveCorridorRequest request) {
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
