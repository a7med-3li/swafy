package com.swafy.auth.controller;

import java.util.UUID;
import com.swafy.auth.dto.AuthResponse;
import com.swafy.auth.dto.LoginRequest;
import com.swafy.auth.dto.TokenRefreshRequest;
import com.swafy.auth.dto.TokenRefreshResponse;
import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.auth.entity.RefreshToken;
import com.swafy.auth.security.SecurityUser;
import com.swafy.auth.service.AuthService;
import com.swafy.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

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
        refreshTokenService.deleteRefreshToken(securityUser.user().getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(securityUser.user().getId());
        
        return ResponseEntity.ok(new AuthResponse(tokenValue, refreshToken.getToken()));
    }
    
    // todo: needs review
    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal String userId) {
        // Invalidate the refresh token on logout
        refreshTokenService.deleteRefreshToken(UUID.fromString(userId));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();
        
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    SecurityUser securityUser = new SecurityUser(user);
                    
                    String newAccessToken = authService.generateToken(securityUser);
                    
                    return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, requestRefreshToken, "Bearer"));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

}
