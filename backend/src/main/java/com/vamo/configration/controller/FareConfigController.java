package com.vamo.configration.controller;

import com.vamo.configration.dto.FareConfigResponse;
import com.vamo.configration.dto.SaveFareConfigRequest;
import com.vamo.configration.service.FareConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/configurations")
@PreAuthorize("hasRole('ADMIN')")
public class FareConfigController {

	private final FareConfigService fareConfigService;

	@GetMapping("/fare-configs")
	public ResponseEntity<List<FareConfigResponse>> getAll() {
		return ResponseEntity.ok(fareConfigService.getAll());
	}

	@PostMapping("/fare-configs")
	public ResponseEntity<FareConfigResponse> create(
			@Valid @RequestBody SaveFareConfigRequest request) {
		return ResponseEntity.ok(fareConfigService.create(request));
	}
}