package com.swafy.common.dto;

import com.swafy.common.enums.Gender;

public record PassengerRegisterRequest(
		String firstName,
		String lastName,
		String phoneNumber,
		Gender gender,
		String password
) {}
