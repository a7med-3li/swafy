package com.vamo.auth.dto;

public record TokenRefreshResponse(
	String accessToken,
	String refreshToken,
	String tokenType
){}
