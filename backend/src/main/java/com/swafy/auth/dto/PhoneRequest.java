package com.swafy.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneRequest(
		@NotBlank @Pattern(regexp = "^[+]?[0-9]{10,15}$") String phone
) {}