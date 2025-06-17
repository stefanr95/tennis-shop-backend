package com.tennis.security;

import com.tennis.model.User;
import com.tennis.repository.UserRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(usernameOrEmail).or(() -> userRepository.findByUsername(usernameOrEmail))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		user.getRoles().forEach(role -> System.out.println("➡️ Role korisnika: " + role.getName().name()));

		List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(role -> {
			String roleName = role.getName().name();
			if (!roleName.startsWith("ROLE_")) {
				return new SimpleGrantedAuthority("ROLE_" + roleName);
			}
			return new SimpleGrantedAuthority(roleName);
		}).toList();

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}