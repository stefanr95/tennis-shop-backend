package com.tennis.service;

import com.tennis.model.RefreshToken;
import com.tennis.model.User;
import com.tennis.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createRefreshToken_shouldReturnSavedToken() {
		User user = new User();
		user.setEmail("test@example.com");

		@SuppressWarnings("unused")
		RefreshToken tokenToSave = RefreshToken.builder().user(user).token("random-token")
				.expiryDate(Instant.now().plusMillis(30L * 24 * 60 * 60 * 1000)).build();

		when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

		RefreshToken savedToken = refreshTokenService.createRefreshToken(user);

		assertNotNull(savedToken);
		assertEquals(user, savedToken.getUser());
		assertNotNull(savedToken.getToken());
		assertTrue(savedToken.getExpiryDate().isAfter(Instant.now()));
		verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
	}

	@Test
	void findByToken_shouldReturnTokenIfExists() {
		RefreshToken token = new RefreshToken();
		token.setToken("token123");

		when(refreshTokenRepository.findByToken("token123")).thenReturn(Optional.of(token));

		Optional<RefreshToken> result = refreshTokenService.findByToken("token123");

		assertTrue(result.isPresent());
		assertEquals("token123", result.get().getToken());
	}

	@Test
	void verifyExpiration_shouldThrowIfExpired() {
		RefreshToken expiredToken = new RefreshToken();
		expiredToken.setExpiryDate(Instant.now().minusSeconds(10));

		assertThrows(RuntimeException.class, () -> refreshTokenService.verifyExpiration(expiredToken));
		verify(refreshTokenRepository, times(1)).delete(expiredToken);
	}

	@Test
	void verifyExpiration_shouldReturnTokenIfNotExpired() {
		RefreshToken validToken = new RefreshToken();
		validToken.setExpiryDate(Instant.now().plusSeconds(1000));

		RefreshToken result = refreshTokenService.verifyExpiration(validToken);

		assertEquals(validToken, result);
		verify(refreshTokenRepository, never()).delete(validToken);
	}
}