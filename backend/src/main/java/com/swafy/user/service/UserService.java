package com.swafy.user.service;

import com.swafy.common.enums.UserRole;
import com.swafy.common.exception.UserAlreadyDeletedException;
import com.swafy.common.exception.UserAlreadyExistsException;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.util.Helpers;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserRegistrationRequest;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(encodedPassword);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponse deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (user.isDeleted()) {
            throw new UserAlreadyDeletedException("User already deleted");
        }

        // Soft delete
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        User deletedUser = userRepository.save(user);

        return mapToResponse(deletedUser);
    }

    public UserResponse restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!user.isDeleted()) {
            throw new IllegalStateException("User is not deleted");
        }

        user.setDeleted(false);
        user.setDeletedAt(null);

        User restoredUser = userRepository.save(user);

        return mapToResponse(restoredUser);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: %d".formatted(id)));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    // rewrite this to follow the best practice
    public User updateUserPartially(Long id, UpdateUserRequest dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Helpers.copyNonNullProperties(dto, user);
        return userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .displayName(user.getFirstName() + " " + user.getLastName().charAt(0) + ".")
                .phoneNumber(maskPhoneNumber(user.getPhoneNumber()))
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
    private String maskPhoneNumber(String phone) {
        if (phone.length() > 4) {
            return phone.substring(0, 2) + "******" + phone.substring(phone.length() - 3);
        }
        return phone;
    }
}