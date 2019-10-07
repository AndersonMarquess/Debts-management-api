package com.andersonmarques.debts_api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.models.UserBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate client;
	@Autowired
	private MongoTemplate mongoTemplate;
	private HttpHeaders headers;
	private UserControllerBuilder userControllerBuilder;
	private DebtControllerBuilder debtControllerBuilder;
	private static final String AUTHORIZATION = "Authorization";

	@BeforeEach
	public void setupObjects() {
		mongoTemplate.getDb().drop();
		headers = new HttpHeaders();
		headers.add("content-Type", "application/json");
		headers.add("accept", "*/*");
		this.userControllerBuilder = new UserControllerBuilder(client, headers);
		this.debtControllerBuilder = new DebtControllerBuilder(client, headers);
	}

	@Test
	public void createAccountWithSuccess() {
		User user = new UserBuilder().build();
		ResponseEntity<User> postUser = userControllerBuilder.withUser(user).post();

		assertEquals(201, postUser.getStatusCodeValue());
		assertNotNull(postUser.getBody());
		assertEquals(user.getEmail(), postUser.getBody().getEmail());
	}

	@Test
	public void notAllowCreateUserWithoutName() {
		User user = new UserBuilder().withName("").build();
		ResponseEntity<User> postUser = userControllerBuilder.withUser(user).post();

		assertEquals(400, postUser.getStatusCodeValue());
	}

	@Test
	public void receiveJwtTokenAfterLogin() {
		User user = new UserBuilder().build();
		ResponseEntity<User> postUser = userControllerBuilder.withUser(user).post();
		ResponseEntity<Void> loginUser = userControllerBuilder.login(user.getEmail(), "password");

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(200, loginUser.getStatusCodeValue());
		assertTrue(loginUser.getHeaders().containsKey(AUTHORIZATION));
	}

	@Test
	public void notAllowAccessProtectedEndpointWithoutValidJwtToken() {
		User user = new UserBuilder().build();
		ResponseEntity<User> postUser = userControllerBuilder.withUser(user).post();
		ResponseEntity<Void> loginUser = userControllerBuilder.login(user.getEmail(), "password");
		ResponseEntity<Debt> postDebt = debtControllerBuilder.post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(200, loginUser.getStatusCodeValue());
		assertEquals(403, postDebt.getStatusCodeValue());
	}

	@Test
	public void allowAccessProtectedEndpointWithValidJwtToken() {
		User user = new UserBuilder().build();
		ResponseEntity<User> postUser = userControllerBuilder.withUser(user).post();
		ResponseEntity<Void> loginUser = userControllerBuilder.login(user.getEmail(), "password");
		headers.add(AUTHORIZATION, loginUser.getHeaders().getFirst(AUTHORIZATION));
		ResponseEntity<Debt> postDebt = debtControllerBuilder.post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(200, loginUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
	}
}