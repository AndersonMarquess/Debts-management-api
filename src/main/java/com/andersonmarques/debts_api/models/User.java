package com.andersonmarques.debts_api.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class User {
	private String id;
	private String name;
	private String email;
	private String password;
	private Set<String> roles = new HashSet<>();

	public User() {
		this.id = UUID.randomUUID().toString();
	}

	public User(String name, String email, String password) {
		this();
		this.name = name;
		this.email = email;
		this.password = this.hashPassword(password);
	}

	private String hashPassword(String password) {
		if(password.length() != 60 || !password.startsWith("$2a$10$")) {
			return new BCryptPasswordEncoder().encode(password);
		}
		return password;
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		this.password = this.hashPassword(password);
	}

	public Set<String> getRoles() {
		return Collections.unmodifiableSet(this.roles);
	}

	public void addRole(String role) {
		if(!role.toUpperCase().startsWith("ROLE_")) {
			role = "ROLE_"+role;
		}
		this.roles.add(role.toUpperCase());
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", name='" + getName() + "'" +
			", email='" + getEmail() + "'" +
			", password='" + getPassword() + "'" +
			", roles='" + getRoles() + "'" +
			"}";
	}
}