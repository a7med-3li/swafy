package com.swafy.user.dto;

import com.swafy.common.enums.Gender;
import com.swafy.common.enums.UserRole;

public record UserInfo(
        String displayName,
        Gender gender,
        String phoneNumber,
        String email,
        UserRole role
) {}