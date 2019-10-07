package com.andersonmarques.debts_api.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.andersonmarques.debts_api.services.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final Logger logger = LogManager.getLogger("JwtService");
	@Autowired
	private UserService userService;
	@Value("${jwt.expiration}")
	private Long expirationTime;
	@Value("${jwt.secret}")
	private String secret;
	public final String TOKEN_PREFIX = "Bearer ";
	public final String HEADER_KEY = "Authorization";

	public void addAuthenticationToResponse(HttpServletResponse response, String username) {
		logger.info("Tentando gerar token para o usuário: {}", username);
		String token = generateTokenJwt(username);
		response.addHeader(HEADER_KEY, TOKEN_PREFIX + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		logger.info("Token gerado e adicionado ao header com sucesso");
	}

	private String generateTokenJwt(String username) {
		return Jwts.builder()
			.setIssuer("Debts management API")
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
			.signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS384)
			.compact();
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_KEY);
		try {
			String email = Jwts.parser()
				.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody()
				.getSubject();

			return buildAuthentionForEmail(email);
		}catch (Exception e) {
			return null;
		}
	}

	private Authentication buildAuthentionForEmail(String email) {
		UserDetails user = userService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

}
