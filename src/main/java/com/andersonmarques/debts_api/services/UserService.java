package com.andersonmarques.debts_api.services;

import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User create(User user) {
		user.addRole("USER");
		return userRepository.save(user);
	}
}