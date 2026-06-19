package com.swafy.auth.dto;

public record TokenRefreshRequest(
		String refreshToken
) {}
