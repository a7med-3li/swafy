package com.swafy.auth.repository;

import java.util.Optional;
import com.swafy.auth.entity.RefreshToken;
import com.swafy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByToken(String token);

	void deleteByUser(User user);
}
