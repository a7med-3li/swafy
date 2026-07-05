package com.swafy.user.controller;

import com.swafy.common.dto.ApiResponse;
import com.swafy.common.exception.UserNotFoundException;
import com.swafy.common.mapper.Mappers;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserInfo;
import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import com.swafy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getMyInfo(@AuthenticationPrincipal String userId) {
        UserInfo info = userService.getUserInfo(UUID.fromString(userId));
        return ResponseEntity.ok(info);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> usersResponse = userService.getAllUsers();
        return ResponseEntity.ok(usersResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        ApiResponse response = new ApiResponse(true,"User deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable UUID id) {
        UserResponse response = userService.restoreUser(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or principal == #id.toString()")
    @PatchMapping("/{id}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest updateRequest) {
        UserInfo updated = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable UUID id){
        UserInfo info = userService.getUserInfo(id);
        return ResponseEntity.ok(info);
    }
}
