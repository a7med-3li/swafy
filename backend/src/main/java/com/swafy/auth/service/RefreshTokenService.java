package com.swafy.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.swafy.auth.entity.RefreshToken;
import com.swafy.auth.repository.RefreshTokenRepository;
import com.swafy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	@Value("${jwt.refreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	private final RefreshTokenRepository refreshTokenRepository;
	
	private final UserRepository userRepository;
	
	public RefreshToken createRefreshToken(UUID userId) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString()); // Secure random UUID
		
		return refreshTokenRepository.save(refreshToken);
	}
	
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}
	
	@Transactional
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token was expired. Please make a new signin request");
		}
		return token;
	}
}
