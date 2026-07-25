package com.vamo.user.dto;

import com.vamo.common.enums.Gender;
import com.vamo.common.enums.UserRole;

public record UserInfo(
        String displayName,
        Gender gender,
        String phoneNumber,
        String email,
        UserRole role
) {}
