package com.swafy.auth.dto;

import com.swafy.common.enums.Gender;
import com.swafy.common.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegistrationRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private UserRole role;
    private Gender gender;
}
