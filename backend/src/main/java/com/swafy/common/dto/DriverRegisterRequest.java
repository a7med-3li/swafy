package com.swafy.common.dto;

import com.swafy.common.enums.Gender;

public record DriverRegisterRequest(
		String firstName,
		String lastName,
		String phoneNumber,
		Gender gender,
		String nationalId,
		String licenseNumber,
		String password
){}
