package com.andersonmarques.debts_api.controllers;

import javax.validation.Valid;

import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private static final String APPLICATION_JSON = "application/json";
	private static final String BASE_PATH_V1 = "v1/users";

	@Autowired
	private UserService userService;

	@PostMapping(path = BASE_PATH_V1, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<User> adicionar(@Valid @RequestBody User user) {
		user = userService.create(user);
		System.out.println("Novo usuário: "+user);
		return ResponseEntity.status(201).body(user);
	}
}