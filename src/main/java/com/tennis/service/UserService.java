package com.tennis.service;

import com.tennis.model.ERole;
import com.tennis.model.Role;
import com.tennis.model.User;
import com.tennis.payload.RegisterRequest;
import com.tennis.repository.RoleRepository;
import com.tennis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerNewUser(RegisterRequest registerRequest) {
		if (userRepository.existsByUsername(registerRequest.getEmail())) {
			throw new RuntimeException("Email already in use");
		}

		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

		Role userRole = roleRepository.findByName(ERole.USER)
				.orElseThrow(() -> new RuntimeException("Error: ROLE_USER not found."));
		user.setRoles(Set.of(userRole));

		return userRepository.save(user);
	}

	public User registerNewAdmin(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

		Role adminRole = roleRepository.findByName(ERole.ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: ADMIN not found."));

		user.setRoles(Set.of(adminRole));

		return userRepository.save(user);
	}
}