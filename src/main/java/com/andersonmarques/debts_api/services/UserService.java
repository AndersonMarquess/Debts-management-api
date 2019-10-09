package com.andersonmarques.debts_api.services;

import java.util.Optional;

import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.models.UserDetailsImp;
import com.andersonmarques.debts_api.repositories.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	private static final Logger logger = LogManager.getLogger("UserService");
	@Autowired
	private UserRepository userRepository;

	public User create(User user) {
		user.addRole("USER");
		return userRepository.save(user);
	}

	protected User findByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
			logger.info("Usuário com e-mail: [{}] não encontrado", email);
			throw new UsernameNotFoundException("Credenciais inválidas");
		}
		return user.get();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.info("Buscando usuário com e-mail: {}", email);
		return new UserDetailsImp(findByEmail(email));
	}

	public String findUserIdByEmail(String email) {
		return findByEmail(email).getId();
	}

	public String getAuthenticatedUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return findUserIdByEmail(auth.getName());
	}
}