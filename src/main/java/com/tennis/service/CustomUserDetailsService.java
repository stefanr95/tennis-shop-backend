package com.tennis.service;

import com.tennis.model.User;
import com.tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(usernameOrEmail).or(() -> userRepository.findByUsername(usernameOrEmail))
				.orElseThrow(() -> new UsernameNotFoundException(
						"User not found with email or username: " + usernameOrEmail));

		Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}