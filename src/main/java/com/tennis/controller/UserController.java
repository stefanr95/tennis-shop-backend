package com.tennis.controller;

import com.tennis.model.User;
import com.tennis.payload.RegisterRequest;
import com.tennis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody RegisterRequest registerRequest) {
		User newUser = userService.registerNewUser(registerRequest);
		return ResponseEntity.ok(newUser);
	}
}