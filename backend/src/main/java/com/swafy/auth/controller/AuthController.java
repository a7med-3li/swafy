package com.swafy.auth.controller;

import com.swafy.auth.dto.LoginRequest;
import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.auth.service.AuthService;
import com.swafy.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    // TODO: add endpoints for /auth/login and /auth/register
    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody UserRegistrationRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public UserResponse loginUser(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
