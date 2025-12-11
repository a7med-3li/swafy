package com.swafy.common.dto;

import com.swafy.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class ApiResponse {
    private boolean success;
    private UserResponse data;
    private String message;

}
