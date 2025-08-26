package com.tennis.controller;

import com.tennis.model.RefreshToken;
import com.tennis.model.User;
import com.tennis.payload.JwtResponse;
import com.tennis.payload.LoginRequest;
import com.tennis.payload.RegisterRequest;
import com.tennis.security.JwtTokenProvider;
import com.tennis.service.RefreshTokenService;
import com.tennis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtTokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;

	public AuthController(AuthenticationManager authenticationManager, UserService userService,
			JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenProvider = tokenProvider;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
		User user = userService.registerNewUser(request);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String username = authentication.getName();
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		String jwt = tokenProvider.generateToken(username, roles);

		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/register-admin")
	public ResponseEntity<User> registerAdmin(@RequestBody RegisterRequest request) {
		User admin = userService.registerNewAdmin(request);
		return ResponseEntity.ok(admin);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
		String requestRefreshToken = request.get("refreshToken");

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String newAccessToken = tokenProvider.generateToken(user.getUsername(),
							user.getRoles().stream().map(role -> role.getName().name()).toList());
					return ResponseEntity
							.ok(Map.of("accessToken", newAccessToken, "refreshToken", requestRefreshToken));
				}).orElseThrow(() -> new RuntimeException("Refresh token not found"));
	}
}