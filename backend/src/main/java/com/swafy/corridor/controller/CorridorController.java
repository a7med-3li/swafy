package com.swafy.corridor.controller;

import java.util.List;
import com.swafy.corridor.dto.CorridorResponse;
import com.swafy.corridor.dto.SaveCorridorRequest;
import com.swafy.corridor.dto.StopResponse;
import com.swafy.corridor.entity.Corridor;
import com.swafy.corridor.service.CorridorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corridors")
public class CorridorController {
	
	private final CorridorService corridorService;
	
	@PostMapping("/add")
	public ResponseEntity<?> createCorridor(@RequestBody SaveCorridorRequest request) {
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
