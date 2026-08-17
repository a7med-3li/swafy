package com.vamo.auth.controller;

import com.vamo.auth.dto.PhoneRequest;
import com.vamo.auth.dto.VerifyOtpRequest;
import com.vamo.auth.service.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/phone")
public class OTPController {

    private final OTPService otpService;

    @PostMapping("/send-otp")
    public void sendOtp(@Valid @RequestBody PhoneRequest request) {
        otpService.sendOtp(request.phone());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        boolean verified = otpService.verifyOtp(
                request.phone(),
                request.otp()
        );

        if (verified) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired OTP");
        }
    }
}
