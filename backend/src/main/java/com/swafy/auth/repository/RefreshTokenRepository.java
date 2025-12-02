package com.swafy.auth.repository;

import com.swafy.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // custom queries if needed
}
