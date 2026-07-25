package com.vamo.user.dto;

import com.vamo.common.enums.Gender;
import com.vamo.common.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String displayName,
        String phoneNumber,
        UserRole role,
        Gender gender,
        Instant createdAt,
        boolean deleted
) {}
