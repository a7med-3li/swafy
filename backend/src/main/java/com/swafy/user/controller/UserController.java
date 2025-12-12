package com.swafy.user.controller;

import com.swafy.common.dto.ApiResponse;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.auth.dto.UserRegistrationRequest;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    // TODO: add /me endpoints
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> usersResponse = userService.getAllUsers();
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        UserResponse userResponse = userService.deleteUser(id);
        ApiResponse response = new ApiResponse(true,userResponse,"User deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Optional: Restore endpoint
    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable Long id) {
        UserResponse response = userService.restoreUser(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable Long id, @RequestBody UpdateUserRequest dto) {
        User updated = userService.updateUserPartially(id, dto);
        return ResponseEntity.ok(updated);
    }

}
