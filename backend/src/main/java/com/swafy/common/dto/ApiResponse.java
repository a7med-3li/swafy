package com.swafy.common.dto;

import com.swafy.user.dto.UserResponse;

public record ApiResponse(boolean success, String message) {}
