package com.vamo.user.repository;

import com.vamo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByPhoneNumber(String phoneNumber);

    // Find only active (non-deleted) users
    @Query("SELECT u FROM User u WHERE u.phoneNumber = ?1 AND u.deleted = false")
    Optional<User> findActiveByPhoneNumber(String phoneNumber);
    
    Optional<User> findByPhoneNumber(String phoneNumber);

    // Find all active users
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActive();

    // Check if active user exists by phoneNumber
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.phoneNumber = ?1 AND u.deleted = false")
    boolean existsActiveByPhoneNumber(String phoneNumber);

    long countByRole(com.vamo.common.enums.UserRole role);
}
