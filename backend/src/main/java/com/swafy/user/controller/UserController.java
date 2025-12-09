package com.swafy.user.controller;

import com.swafy.common.exception.UserNotFoundException;
import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserDto;
import com.swafy.user.entity.Users;
import com.swafy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    // TODO: add /me endpoints
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public Users getUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @PostMapping("/")
    public Users addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Users> updateUserPartially(@PathVariable Long id, @RequestBody UpdateUserRequest dto) {
        Users updated = userService.updateUserPartially(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest dto) {
        Users updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

}
