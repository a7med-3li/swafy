package com.swafy.user.repository;

import com.swafy.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // findByUsername, etc.
}
