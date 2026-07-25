package com.vamo.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
		@NotBlank String refreshToken
) {}
