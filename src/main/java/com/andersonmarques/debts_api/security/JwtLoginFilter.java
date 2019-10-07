package com.andersonmarques.debts_api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.andersonmarques.debts_api.models.AccountCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LogManager.getLogger("JwtLoginFilter");
	private JwtService jwtService;

	public JwtLoginFilter(AuthenticationManager authManager, JwtService JwtService) {
		super("/login");
		this.jwtService = JwtService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		AccountCredentials credentials = new ObjectMapper().readValue(request.getInputStream(),
				AccountCredentials.class);

		logger.info("Autenticando usuário: {}", credentials.getEmail());
		return getAuthenticationManager().authenticate(credentials.generateAuthenticationToken());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		logger.info("Usuário: {} autenticado com sucesso", authResult.getName());
		jwtService.addAuthenticationToResponse(response, authResult.getName());
	}
}