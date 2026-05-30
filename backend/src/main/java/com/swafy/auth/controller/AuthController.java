package com.swafy.auth.controller;

import com.swafy.auth.dto.AuthResponse;
import com.swafy.auth.dto.LoginRequest;
import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.auth.security.SecurityUser;
import com.swafy.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        authService.registerUser(request);
        return login(new LoginRequest(request.email(), request.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Authenticated user: " + loginRequest.email() + " " + loginRequest.email());
        SecurityUser securityUser = authService.authenticate(
                loginRequest.email(),
                loginRequest.password()
        );
        String tokenValue = authService.generateToken(securityUser);
        return ResponseEntity.ok(new AuthResponse(tokenValue));
    }

}
