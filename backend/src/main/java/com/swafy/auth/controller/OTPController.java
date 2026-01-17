package com.swafy.auth.controller;

import com.swafy.auth.dto.PhoneRequest;
import com.swafy.auth.dto.VerifyOtpRequest;
import com.swafy.auth.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth/phone")
public class OTPController {

    private final OTPService otpService;

    @PostMapping("/send-otp")
    public void sendOtp(@RequestBody PhoneRequest request) {
        otpService.sendOtp(request.getPhone());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        boolean verified = otpService.verifyOtp(
                request.getPhone(),
                request.getOtp()
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
