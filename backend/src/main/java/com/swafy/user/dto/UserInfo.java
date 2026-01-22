package com.swafy.user.dto;

import com.swafy.common.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfo {

    private String displayName;
    private Gender gender;
    private String phoneNumber;
    private String email;

}
