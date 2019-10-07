package com.andersonmarques.debts_api.controllers;

import com.andersonmarques.debts_api.models.AccountCredentials;
import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.models.UserBuilder;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class UserControllerBuilder {
	private TestRestTemplate client;
	private HttpHeaders headers;
	private User user;

	public UserControllerBuilder(TestRestTemplate client, HttpHeaders headers) {
		this.client = client;
		this.headers = headers;
		this.user = new UserBuilder().build();
	}

	public UserControllerBuilder withUser(User user) {
		this.user = user;
		return this;
	}

	public ResponseEntity<User> post() {
		return client.postForEntity("/v1/users", new HttpEntity<User>(user, headers), User.class);
	}

	public ResponseEntity<Void> login(String email, String password) {
		AccountCredentials account = new AccountCredentials();
		account.setEmail(email);
		account.setPassword(password);
		return client.postForEntity("/login", new HttpEntity<AccountCredentials>(account, headers), Void.class);
	}
}