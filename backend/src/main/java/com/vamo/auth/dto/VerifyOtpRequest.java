package com.vamo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyOtpRequest(
		@NotBlank @Pattern(regexp = "^[+]?[0-9]{10,15}$") String phone,
		@NotBlank @Size(min = 4, max = 8) String otp
) {}
