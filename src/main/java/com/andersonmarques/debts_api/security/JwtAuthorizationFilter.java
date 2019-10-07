package com.andersonmarques.debts_api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private static final Logger logger = LogManager.getLogger("JwtAuthorizationFilter");
	private JwtService jwtService;

	public JwtAuthorizationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("Verificando token para o request: {}", request.getLocalAddr());
		Authentication authentication = jwtService.getAuthentication(request);

		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			logger.info("Token do usuário: [{}] verificado com sucesso", authentication.getName());
		} else {
			logger.info("Token inexistente ou inválido");
		}

		filterChain.doFilter(request, response);
	}
}