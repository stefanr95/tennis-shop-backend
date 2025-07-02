package com.tennis.service;

import com.tennis.model.ERole;
import com.tennis.model.Role;
import com.tennis.model.User;
import com.tennis.payload.RegisterRequest;
import com.tennis.repository.RoleRepository;
import com.tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public User registerNewUser(RegisterRequest registerRequest) {
		validateUserUniqueness(registerRequest);
		return createUserWithRole(registerRequest, ERole.USER);
	}

	public User registerNewAdmin(RegisterRequest registerRequest) {
		validateUserUniqueness(registerRequest);
		return createUserWithRole(registerRequest, ERole.ADMIN);
	}

	private void validateUserUniqueness(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already in use.");
		}
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new RuntimeException("Username already in use.");
		}
	}

	private User createUserWithRole(RegisterRequest request, ERole roleName) {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(Set.of(role));

		return userRepository.save(user);
	}
}