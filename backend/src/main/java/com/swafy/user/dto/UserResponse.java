package com.swafy.user.dto;

import com.swafy.common.enums.Gender;
import com.swafy.common.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String email,
        String displayName,
        String phoneNumber,
        UserRole role,
        Gender gender,
        LocalDateTime createdAt,
        boolean deleted
) {}