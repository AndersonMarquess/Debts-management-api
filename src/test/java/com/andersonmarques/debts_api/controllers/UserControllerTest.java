package com.andersonmarques.debts_api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.andersonmarques.debts_api.models.User;
import com.andersonmarques.debts_api.models.UserBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate client;
	private HttpHeaders headers;
	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setupObjects() {
		headers = new HttpHeaders();
		headers.add("content-Type", "application/json");
		headers.add("accept", "*/*");
		mongoTemplate.getDb().drop();
	}

	public ResponseEntity<User> postUser(User user) {
		return client.postForEntity("/v1/users", new HttpEntity<User>(user, headers), User.class);
	}

	@Test
	public void createAccountWithSuccess() {
		User user = new UserBuilder().build();
		ResponseEntity<User> postUser = postUser(user);

		assertEquals(201, postUser.getStatusCodeValue());
		assertNotNull(postUser.getBody());
		assertEquals(user.getEmail(), postUser.getBody().getEmail());
	}

	@Test
	public void notAllowCreateUserWithoutName() {
		User user = new UserBuilder().withName("").build();
		ResponseEntity<User> postUser = postUser(user);

		assertEquals(400, postUser.getStatusCodeValue());
	}
}