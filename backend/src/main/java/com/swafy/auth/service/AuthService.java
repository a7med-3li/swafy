package com.swafy.auth.service;

import com.swafy.auth.dto.LoginRequest;
import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.common.exception.UserAlreadyExistsException;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.exception.WrongPasswordException;
import com.swafy.driver.service.DriverService;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.repository.UserRepository;
import com.swafy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.swafy.common.util.Helpers.mapToResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        return mapToResponse(userService.createUser(createUser(request, passwordEncoder)));
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Please register first!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new WrongPasswordException("Wrong password");
        }

        //String token = jwtService.generateToken(user);

        return mapToResponse(user);
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
        return user;
    }
}
