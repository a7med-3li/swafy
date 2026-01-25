package com.swafy.user.dto;

import com.swafy.common.enums.Gender;
import com.swafy.common.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    // TODO: Refine this after implementing the refresh token
    private UUID userId;
    private String email;
    private String displayName;
    private String phoneNumber;
    private UserRole role;
    private Gender gender;
    private LocalDateTime createdAt;
    private boolean deleted = false;
}
