package com.swafy.user.service;

import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.util.Helpers;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserDto;
import com.swafy.user.entity.Users;
import com.swafy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // TODO: implement user operations
    public Users addUser(UserDto dto){
        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    public Users getUserById(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: %d".formatted(id)));
    }
    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }
    public Users updateUserPartially(Long id, UpdateUserRequest dto){
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Helpers.copyNonNullProperties(dto, user);
        return userRepository.save(user);
    }
    public Users updateUser(Long id, UpdateUserRequest dto){
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(dto.getDisplayName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }
}
