package com.tennis.service;

import com.tennis.model.ERole;
import com.tennis.model.Role;
import com.tennis.model.User;
import com.tennis.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void loadUserByUsername_shouldReturnUserDetails_whenUserExistsByEmail() {
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("password");
		user.setRoles(Set.of(new Role(null, ERole.USER)));

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

		assertNotNull(userDetails);
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals("password", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
	}

	@Test
	void loadUserByUsername_shouldReturnUserDetails_whenUserExistsByUsername() {
		User user = new User();
		user.setEmail("user@example.com");
		user.setPassword("password");
		user.setRoles(Set.of(new Role(null, ERole.USER)));

		when(userRepository.findByEmail("testuser")).thenReturn(Optional.empty());
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

		assertNotNull(userDetails);
		assertEquals("user@example.com", userDetails.getUsername());
	}

	@Test
	void loadUserByUsername_shouldThrow_whenUserNotFound() {
		when(userRepository.findByEmail("unknown")).thenReturn(Optional.empty());
		when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("unknown"));
	}
}