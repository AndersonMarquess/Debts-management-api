package com.andersonmarques.debts_api.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserBuilder {

	private String id;
	private String nome;
	private String email;
	private String password;
	private Set<SimpleGrantedAuthority> roles = new HashSet<>();

	public UserBuilder() {
		this.id = UUID.randomUUID().toString();
		this.nome = "anderson";
		this.email = "anderson@email.com";
		this.password = "password";
		this.roles.add(new SimpleGrantedAuthority("USER"));
	}

	public User build() {
		return new User(this.id, this.nome, this.email, this.password, this.roles);
	}

	public UserBuilder withRole(String role) {
		this.roles.add(new SimpleGrantedAuthority(role.toUpperCase()));
		return this;
	}

	public UserBuilder withPassword(String password) {
		this.password = password;
		return this;
	}
}
