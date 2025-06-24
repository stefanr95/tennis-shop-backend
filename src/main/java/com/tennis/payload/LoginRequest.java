package com.tennis.payload;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "Username or email is required")
	private String usernameOrEmail;

	@NotBlank(message = "Password is required")
	private String password;

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}