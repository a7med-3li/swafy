package com.swafy.user.service;

import com.swafy.common.exception.UserAlreadyDeletedException;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.util.Helpers;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.swafy.common.util.Helpers.mapToResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user){
        return userRepository.save(user);
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

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: %d".formatted(id)));

        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User user : users){
            userResponseList.add(mapToResponse(user));
        }
        return userResponseList;
    }
    // rewrite this to follow the best practice
    public User updateUser(Long id, UpdateUserRequest dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Helpers.copyNonNullProperties(dto, user);
        return userRepository.save(user);
    }
}