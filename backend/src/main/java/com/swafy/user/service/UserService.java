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

    public void saveUser(User user){
        userRepository.save(user);
    }

    // todo: edit the return object to match the best practice, and this might include token expiration.
    public UserResponse deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (user.isDeleted()) {
            throw new UserAlreadyDeletedException("User already deleted");
        }

        userRepository.delete(user);

        return mapToResponse(user);
    }

    // note: needs logic review
    public UserResponse restoreUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!user.isDeleted()) {
            throw new IllegalStateException("User is not deleted");
        }

        user.setDeleted(false);
        user.setDeletedAt(null);

        User restoredUser = userRepository.save(user);

        // todo: create a mapper
        return mapToResponse(restoredUser);
    }

    public UserResponse getUserById(UUID id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id:" + id));
        // todo: replace with object mapper
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
    // todo: rewrite this to follow the best practice
    public User updateUser(UUID id, UpdateUserRequest dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (dto.firstName() != null)   user.setFirstName(dto.firstName());
        if (dto.lastName() != null)    user.setLastName(dto.lastName());
        if (dto.phoneNumber() != null) user.setPhoneNumber(dto.phoneNumber());
        if (dto.gender() != null)      user.setGender(dto.gender());

        return userRepository.save(user);
    }

    private UserInfo populateUserInfo(User user) {
        return new UserInfo(
                user.getFirstName() + " " + user.getLastName().charAt(0) + ".",
                user.getGender(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getRole()
        );
    }
}
