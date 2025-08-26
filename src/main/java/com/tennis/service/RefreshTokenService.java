package com.tennis.service;

import com.tennis.model.RefreshToken;
import com.tennis.model.User;
import com.tennis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	private final long refreshTokenDurationMs = 30L * 24 * 60 * 60 * 1000;

	public RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = RefreshToken.builder().user(user).token(UUID.randomUUID().toString())
				.expiryDate(Instant.now().plusMillis(refreshTokenDurationMs)).build();

		return refreshTokenRepository.save(refreshToken);
	}

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token expired. Please login again.");
		}
		return token;
	}
}