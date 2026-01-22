package com.swafy.user.dto;

import com.swafy.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
}
