package com.swafy.auth.service;

import com.swafy.auth.dto.LoginRequest;
import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.auth.security.SecurityUser;
import com.swafy.common.exception.UserAlreadyExistsException;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.exception.WrongPasswordException;
import com.swafy.driver.service.DriverService;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.repository.UserRepository;
import com.swafy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static com.swafy.common.util.Helpers.mapToResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    // TODO: Add the refresh token logic to prevent session expiration every 15m as well

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtEncoder jwtEncoder;
    
    public SecurityUser authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return (SecurityUser) userDetailsService.loadUserByUsername(email);
    }
    
    public String generateToken(SecurityUser securityUser) {
        Instant now = Instant.now();
        
        String scope = securityUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .subject(securityUser.user().getId().toString())
                .claim("roles", scope)
                .build();
        
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public void registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        userService.createUser(createUser(request, passwordEncoder));
    }

    public static User createUser(UserRegistrationRequest request, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(encodedPassword);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(request.getRole());
        user.setGender(request.getGender());
        return user;
    }
}
