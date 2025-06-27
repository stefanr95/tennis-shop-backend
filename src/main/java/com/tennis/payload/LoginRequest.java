package com.tennis.payload;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "Username or email is required.")
	private String usernameOrEmail;

	@NotBlank(message = "Password is required.")
	private String password;

	public LoginRequest() {
	}

	public LoginRequest(String usernameOrEmail, String password) {
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
	}

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}