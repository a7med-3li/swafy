package com.swafy.common.dto;

import com.swafy.user.dto.UserResponse;

public record ApiResponse(boolean success, UserResponse data, String message) {}