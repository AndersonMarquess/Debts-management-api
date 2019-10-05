package com.andersonmarques.debts_api.controllers;

import javax.validation.Valid;

import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.services.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private static final Logger logger = LogManager.getLogger("UserController");
	private static final String APPLICATION_JSON = "application/json";
	private static final String BASE_PATH_V1 = "v1/users";

	@Autowired
	private UserService userService;

	@PostMapping(path = BASE_PATH_V1, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<User> create(@Valid @RequestBody User user) {
		logger.info("Tentando criar novo usuário: {}", user);
		user = userService.create(user);
		logger.info("Novo usuário criado com sucesso: {}", user);
		return ResponseEntity.status(201).body(user);
	}
}