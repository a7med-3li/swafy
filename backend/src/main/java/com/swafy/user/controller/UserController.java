package com.swafy.user.controller;

import com.swafy.common.dto.ApiResponse;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserInfo;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable UUID id){
        UserInfo info = userService.getUserInfo(id);
        return ResponseEntity.ok(info);
    }

    // todo: should be paginated and add PreAuthorize
    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> usersResponse = userService.getAllUsers();
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        UserResponse userResponse = userService.deleteUser(id);
        ApiResponse response = new ApiResponse(true,userResponse,"User deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable UUID id) {
        UserResponse response = userService.restoreUser(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest updateRequest) {
        User updated = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updated);
    }
}
