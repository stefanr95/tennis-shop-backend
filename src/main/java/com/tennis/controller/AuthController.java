package com.tennis.controller;

import com.tennis.model.User;
import com.tennis.payload.JwtResponse;
import com.tennis.payload.LoginRequest;
import com.tennis.payload.RegisterRequest;
import com.tennis.security.JwtTokenProvider;
import com.tennis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtTokenProvider tokenProvider;
	@SuppressWarnings("unused")
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserService userService,
			JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenProvider = tokenProvider;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(loginRequest.getUsernameOrEmail());

		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody RegisterRequest registerRequest) {
		User newUser = userService.registerNewUser(registerRequest);
		return ResponseEntity.ok(newUser);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/register-admin")
	public ResponseEntity<User> registerAdmin(@RequestBody RegisterRequest registerRequest) {
		User newAdmin = userService.registerNewAdmin(registerRequest);
		return ResponseEntity.ok(newAdmin);
	}
}