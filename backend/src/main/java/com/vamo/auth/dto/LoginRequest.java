package com.vamo.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        String phoneNumber,
        
        @NotBlank(message = "Password is required")
        String password
) {
}
