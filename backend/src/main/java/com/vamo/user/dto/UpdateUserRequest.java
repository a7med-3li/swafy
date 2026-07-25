package com.vamo.user.dto;

import com.vamo.common.enums.Gender;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 2, max = 50) String firstName,
        @Size(min = 2, max = 50) String lastName,
        @Pattern(regexp = "^[+]?[0-9]{10,15}$") String phoneNumber,
        Gender gender
) {}
