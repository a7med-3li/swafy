package com.vamo.auth.repository;

import java.util.Optional;
import com.vamo.auth.entity.RefreshToken;
import com.vamo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByToken(String token);

	void deleteByUser(User user);
}
