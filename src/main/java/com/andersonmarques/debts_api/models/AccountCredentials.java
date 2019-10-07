package com.andersonmarques.debts_api.models;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AccountCredentials {

	private String email;
	private String password;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UsernamePasswordAuthenticationToken generateAuthenticationToken() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}
}