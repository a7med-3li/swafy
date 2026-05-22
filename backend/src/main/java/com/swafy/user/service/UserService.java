package com.swafy.user.service;

import com.swafy.common.exception.UserAlreadyDeletedException;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.util.Helpers;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserInfo;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.swafy.common.util.Helpers.mapToResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfo getUserInfo(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return populateUserInfo(user);
    }

    public void createUser(User user){
        userRepository.save(user);
    }

    public UserResponse deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (user.isDeleted()) {
            throw new UserAlreadyDeletedException("User already deleted");
        }

        userRepository.delete(user);

        return mapToResponse(user);
    }

    public UserResponse restoreUser(UUID id) {
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

    public UserResponse getUserById(UUID id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id:" + id));

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
    public User updateUser(UUID id, UpdateUserRequest dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Helpers.copyNonNullProperties(dto, user);
        return userRepository.save(user);
    }

    private UserInfo populateUserInfo(User user){
        return UserInfo.builder()
                .email(user.getEmail())
                .displayName(user.getFirstName() + " " + user.getLastName().charAt(0) + ".")
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
    }
}
