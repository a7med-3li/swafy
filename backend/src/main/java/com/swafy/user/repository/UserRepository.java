package com.swafy.user.repository;

import com.swafy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // Optional: case-insensitive email check
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    // Find only active (non-deleted) users
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.deleted = false")
    Optional<User> findActiveByEmail(String email);

    // Find all active users
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActive();

    // Check if active user exists by email
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1 AND u.deleted = false")
    boolean existsActiveByEmail(String email);
}
