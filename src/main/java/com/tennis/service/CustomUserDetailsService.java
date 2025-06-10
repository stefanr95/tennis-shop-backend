package com.tennis.service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.tennis.model.User;
import com.tennis.repository.UserRepository;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		System.out.println("⏩ Pozvana metoda loadUserByUsername sa: " + usernameOrEmail);

		Optional<User> userByEmail = userRepository.findByEmail(usernameOrEmail);
		if (userByEmail.isPresent()) {
			System.out.println("✅ Nađen korisnik po email-u");
			User user = userByEmail.get();
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					new ArrayList<>());
		}

		Optional<User> userByUsername = userRepository.findByUsername(usernameOrEmail);
		if (userByUsername.isPresent()) {
			System.out.println("✅ Nađen korisnik po username-u");
			User user = userByUsername.get();
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					new ArrayList<>());
		}

		System.out.println("❌ Nije pronađen korisnik ni po email ni po username.");
		throw new UsernameNotFoundException("User not found with email or username: " + usernameOrEmail);
	}
}