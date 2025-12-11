package com.swafy.user.dto;

import com.swafy.common.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long userId;
    private String email;
    private String displayName;
    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdAt;

}
