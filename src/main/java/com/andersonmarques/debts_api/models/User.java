package com.andersonmarques.debts_api.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class User {
	private String id;
	private String nome;
	private String email;
	private String password;
	private Set<SimpleGrantedAuthority> roles = new HashSet<>();

	public User() {
		this.id = UUID.randomUUID().toString();
	}

	public User(String id, String nome, String email, String password, Set<SimpleGrantedAuthority> roles) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.password = this.hashPassword(password);
		this.roles = roles;
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

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Set<SimpleGrantedAuthority> getRoles() {
		return Collections.unmodifiableSet(this.roles);
	}

	public void addRole(String role) {
		this.roles.add(new SimpleGrantedAuthority(role.toUpperCase()));
	}
}