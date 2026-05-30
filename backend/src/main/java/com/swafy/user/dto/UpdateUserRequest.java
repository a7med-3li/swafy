package com.swafy.user.dto;

import com.swafy.common.enums.Gender;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        Gender gender
) {}