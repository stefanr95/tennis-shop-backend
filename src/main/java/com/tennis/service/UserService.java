package com.tennis.service;

import com.tennis.model.User;
import com.tennis.payload.RegisterRequest;
import com.tennis.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// Registracija novog korisnika
	public User registerNewUser(RegisterRequest registerRequest) {
		if (userRepository.existsByUsername(registerRequest.getUsername())) {
			throw new RuntimeException("Username is already taken.");
		}

		// Kreiranje novog korisnika
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setRole("USER");

		return userRepository.save(user);
	}

	// Metoda za logovanje korisnika
	public Optional<User> loginUser(String username, String password) {
		Optional<User> user = userRepository.findByUsername(username);
		return user.filter(u -> passwordEncoder.matches(password, u.getPassword()));
	}
}