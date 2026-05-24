package com.swafy.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> errors
) {}