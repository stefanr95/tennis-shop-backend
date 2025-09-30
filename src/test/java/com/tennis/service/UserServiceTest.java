package com.tennis.service;

import com.tennis.model.ERole;
import com.tennis.model.Role;
import com.tennis.model.User;
import com.tennis.payload.RegisterRequest;
import com.tennis.repository.RoleRepository;
import com.tennis.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void registerNewUser_shouldSaveUserWithUserRole() {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("testuser");
		request.setEmail("test@example.com");
		request.setPassword("password");

		Role userRole = new Role();
		userRole.setName(ERole.USER);

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
		when(roleRepository.findByName(ERole.USER)).thenReturn(Optional.of(userRole));
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

		User savedUser = userService.registerNewUser(request);

		assertNotNull(savedUser);
		assertEquals("testuser", savedUser.getUsername());
		assertEquals("test@example.com", savedUser.getEmail());
		assertEquals("encodedPassword", savedUser.getPassword());
		assertTrue(savedUser.getRoles().contains(userRole));

		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void registerNewUser_shouldThrowExceptionIfEmailExists() {
		RegisterRequest request = new RegisterRequest();
		request.setEmail("exists@example.com");
		request.setUsername("newuser");

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerNewUser(request));

		assertEquals("Email already in use.", ex.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void registerNewUser_shouldThrowExceptionIfUsernameExists() {
		RegisterRequest request = new RegisterRequest();
		request.setEmail("new@example.com");
		request.setUsername("existingUser");

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerNewUser(request));

		assertEquals("Username already in use.", ex.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void findByUsername_shouldReturnUser() {
		User user = new User();
		user.setUsername("user1");

		when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

		User found = userService.findByUsername("user1");

		assertNotNull(found);
		assertEquals("user1", found.getUsername());
	}

	@Test
	void findByUsername_shouldThrowIfNotFound() {
		when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.findByUsername("unknown"));

		assertEquals("User not found with username: unknown", ex.getMessage());
	}

	@Test
	void findByEmail_shouldReturnUser() {
		User user = new User();
		user.setEmail("user@example.com");

		when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

		User found = userService.findByEmail("user@example.com");

		assertNotNull(found);
		assertEquals("user@example.com", found.getEmail());
	}

	@Test
	void findByEmail_shouldThrowIfNotFound() {
		when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> userService.findByEmail("notfound@example.com"));

		assertEquals("User not found with email: notfound@example.com", ex.getMessage());
	}
}