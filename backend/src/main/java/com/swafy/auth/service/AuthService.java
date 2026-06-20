package com.swafy.auth.service;

import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.auth.security.SecurityUser;
import com.swafy.common.dto.DriverRegisterRequest;
import com.swafy.common.dto.PassengerRegisterRequest;
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
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;


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

    // todo: needs review
    public void registerPassenger(PassengerRegisterRequest request) {
        userService.registerPassenger(request);
    }
    
    public void registerDriver(DriverRegisterRequest request) {
        userService.registerDriver(request);
    }

    public static User createUser(UserRegistrationRequest request, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setCreatedAt(Instant.now());
        user.setRole(request.role());
        user.setGender(request.gender());
        return user;
    }
}
