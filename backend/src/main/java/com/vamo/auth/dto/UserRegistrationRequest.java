package com.vamo.auth.dto;

import com.vamo.common.enums.Gender;
import com.vamo.common.enums.UserRole;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String password,
        UserRole role,
        Gender gender
) {}
