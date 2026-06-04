package com.swafy.user.service;

import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.mapper.Mappers;
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
    private final Mappers mappers;
    
    public UserInfo getUserInfo(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mappers.userToUserInfo(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (user.isDeleted()) { return;}
        
        userRepository.delete(user);
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
    public UserInfo updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        mappers.updateUserFromRequest(request, user);

        return mappers.userToUserInfo(userRepository.save(user));
    }
}
