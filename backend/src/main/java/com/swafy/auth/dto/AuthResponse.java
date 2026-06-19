package com.swafy.auth.dto;

public record AuthResponse(
        String token,
        String refreshToken
) {}
