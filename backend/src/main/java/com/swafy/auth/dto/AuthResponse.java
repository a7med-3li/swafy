package com.swafy.auth.dto;

// todo: this might contain the basic info of the user to present it in the view
public record AuthResponse(
        String token,
        String refreshToken
) {}
