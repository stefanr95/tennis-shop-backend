package com.tennis.controller;

import com.tennis.model.User;
import com.tennis.payload.JwtResponse;
import com.tennis.payload.LoginRequest;
import com.tennis.payload.RegisterRequest;
import com.tennis.security.JwtTokenProvider;
import com.tennis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtTokenProvider tokenProvider;

	public AuthController(AuthenticationManager authenticationManager, UserService userService,
			JwtTokenProvider tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenProvider = tokenProvider;
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
		String jwt = tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/register-admin")
	public ResponseEntity<User> registerAdmin(@RequestBody RegisterRequest request) {
		User admin = userService.registerNewAdmin(request);
		return ResponseEntity.ok(admin);
	}
}