package com.swafy.user.repository;

import com.swafy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // findByUsername, etc.
}
