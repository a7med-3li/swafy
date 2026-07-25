package com.vamo.addressing.controller;

import com.vamo.addressing.dto.AutoCompleteResponse;
import com.vamo.addressing.service.AddressingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressingController {
    private final AddressingService addressingService;
    //TODO: add end-points, search

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/autoComplete")
    public ResponseEntity<AutoCompleteResponse> autoComplete(@RequestParam(required = false) String address){
        return ResponseEntity.ok(addressingService.autoComplete(address));
    }
}
