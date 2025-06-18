package com.tennis.controller;

import com.tennis.model.User;
import com.tennis.payload.RegisterRequest;
import com.tennis.payload.UserResponse;
import com.tennis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		User newUser = userService.registerNewUser(registerRequest);
		UserResponse response = new UserResponse(newUser.getId(), newUser.getUsername(), newUser.getEmail());
		return ResponseEntity.ok(response);
	}
}