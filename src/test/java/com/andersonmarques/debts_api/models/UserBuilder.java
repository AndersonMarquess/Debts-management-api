package com.andersonmarques.debts_api.models;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {

	private String name;
	private String email;
	private String password;
	private Set<String> roles = new HashSet<>();

	public UserBuilder() {
		this.name = "anderson";
		this.email = "anderson@email.com";
		this.password = "password";
		this.roles.add("USER");
	}

	public User build() {
		User user = new User(this.name, this.email, this.password);
		roles.forEach(r -> user.addRole(r));
		return user;
	}

	public UserBuilder withRole(String role) {
		this.roles.add(role);
		return this;
	}

	public UserBuilder withPassword(String password) {
		this.password = password;
		return this;
	}

	public UserBuilder withName(String name) {
		this.name = name;
		return this;
	}
}
