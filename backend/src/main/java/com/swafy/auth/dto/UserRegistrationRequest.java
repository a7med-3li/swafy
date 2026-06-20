package com.swafy.auth.dto;

import com.swafy.common.enums.Gender;
import com.swafy.common.enums.UserRole;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String password,
        UserRole role,
        Gender gender
) {}
