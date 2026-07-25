package com.vamo.user.service;

import com.vamo.common.enums.Gender;
import com.vamo.common.enums.UserRole;
import com.vamo.common.events.DriverRegisteredEvent;
import com.vamo.common.events.PassengerRegisteredEvent;
import com.vamo.common.exception.UserNotFoundException;
import com.vamo.common.mapper.Mappers;
import com.vamo.common.dto.DriverRegisterRequest;
import com.vamo.common.dto.PassengerRegisterRequest;
import com.vamo.user.dto.UpdateUserRequest;
import com.vamo.user.dto.UserInfo;
import com.vamo.user.dto.UserResponse;
import com.vamo.user.entity.User;
import com.vamo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.vamo.common.util.Helpers.mapToResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mappers mappers;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    
    public User registerPassenger(PassengerRegisterRequest req) {
        User user = userRepository.findByPhoneNumber(req.phoneNumber())
                .orElseGet(() -> createBaseUser(req.phoneNumber(), req.firstName(),req.lastName(), UserRole.PASSENGER, req.password(),req.gender()));
        
        if (user.getRole() == UserRole.DRIVER) {
            user.setRole(UserRole.BOTH);
        }
        
        eventPublisher.publishEvent(new PassengerRegisteredEvent(user, req));
        
        return user;
    }
    
    public User registerDriver(DriverRegisterRequest req) {
        User user = userRepository.findByPhoneNumber(req.phoneNumber())
                .orElseGet(() -> createBaseUser(req.phoneNumber(), req.firstName(), req.lastName(), UserRole.DRIVER, req.password(), req.gender()));

        if (user.getRole() == UserRole.PASSENGER) {
            user.setRole(UserRole.BOTH);
        }
        
        eventPublisher.publishEvent(new DriverRegisteredEvent(user, req));
        
        return user;
    }
    
    private User createBaseUser(String phone, String firstName, String lastName, UserRole role, String password, Gender gender) {
        User user = new User();
        user.setPhoneNumber(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setDeleted(false);
        user.setCreatedAt(Instant.now());
        user.setGender(gender);
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
    
    // todo: needs to be refactored to map based on the user role. (response dto TBD)
    public UserInfo getUserInfo(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mappers.userToUserInfo(user);
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

    // todo: this should be altered to get by phone number and for ADMIN only.
    public UserResponse getUserById(UUID id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id:" + id));
        // todo: replace with object mapper
        return mapToResponse(user);
    }

    // note: for admins only
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
