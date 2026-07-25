package com.vamo.common.dto;

import com.vamo.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DriverRegisterRequest(
		@NotBlank @Size(min = 2, max = 50) String firstName,
		@NotBlank @Size(min = 2, max = 50) String lastName,
		@NotBlank @Pattern(regexp = "^[+]?[0-9]{10,15}$") String phoneNumber,
		@NotNull Gender gender,
		@NotBlank @Size(min = 5, max = 50) String nationalId,
		@NotBlank @Size(min = 5, max = 50) String licenseNumber,
		@NotBlank @Size(min = 6, max = 128) String password
){}
