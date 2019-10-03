package com.andersonmarques.debts_api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.models.DebtBuilder;
import com.andersonmarques.debts_api.models.User;

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
public class DebtControllerTest {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TestRestTemplate client;
	private HttpHeaders headers;

	@BeforeEach
	public void setupObjects() {
		mongoTemplate.getDb().drop();
		this.headers = new HttpHeaders();
		headers.add("content-Type", "application/json");
		headers.add("accept", "application/json");
	}

	@Test
	public void notAllowCreateDebtWithoutAValidUser() {
		ResponseEntity<User> postUser = new UserControllerBuilder(client, headers).post();
		Debt debt = new DebtBuilder().withAmount(25d).withInstallment(3).build();
		ResponseEntity<Debt> postDebt = new DebtControllerBuilder(client, headers).withDebt(debt)
				.post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertNotNull(postDebt.getBody());
		assertEquals(25d, postDebt.getBody().getAmount());
		assertEquals(debt.getTotalAmount(), postDebt.getBody().getTotalAmount());
		assertEquals(postUser.getBody().getId(), postDebt.getBody().getOwnerId());
	}

	@Test
	public void notAllowCreateDebtWithZeroOrNegativeAmount() {
		ResponseEntity<User> postUser = new UserControllerBuilder(client, headers).post();
		Debt debt = new DebtBuilder().withAmount(-25d).build();
		ResponseEntity<Debt> postDebt = new DebtControllerBuilder(client, headers).withDebt(debt)
				.post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(400, postDebt.getStatusCodeValue());
	}

	@Test
	public void notAllowCreateDebtWithZeroOrNegativeInstallment() {
		ResponseEntity<User> postUser = new UserControllerBuilder(client, headers).post();
		Debt debt = new DebtBuilder().withInstallment(-1).build();
		ResponseEntity<Debt> postDebt = new DebtControllerBuilder(client, headers).withDebt(debt)
				.post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(400, postDebt.getStatusCodeValue());
	}

	@Test
	public void reduceInstallmentAfterPay() {
		Debt debt = new DebtBuilder().withInstallment(10).build();
		ResponseEntity<User> postUser = new UserControllerBuilder(client, headers).post();
		DebtControllerBuilder debtControllerBuilder = new DebtControllerBuilder(client, headers);
		ResponseEntity<Debt> postDebt = debtControllerBuilder.withDebt(debt).post(postUser.getBody());
		ResponseEntity<Debt> payDebt = debtControllerBuilder.withDebt(postDebt.getBody()).pay();

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertEquals(200, payDebt.getStatusCodeValue());
		assertEquals(10, debt.getInstallment());
		assertNotNull(payDebt.getBody());
		assertEquals(9, payDebt.getBody().getInstallment());
	}

	@Test
	public void removeDebtAfterPayLastInstallment() {
		Debt debt = new DebtBuilder().withInstallment(2).build();
		ResponseEntity<User> postUser = new UserControllerBuilder(client, headers).post();
		DebtControllerBuilder debtControllerBuilder = new DebtControllerBuilder(client, headers);
		ResponseEntity<Debt> postDebt = debtControllerBuilder.withDebt(debt).post(postUser.getBody());
		ResponseEntity<Debt> payDebt = debtControllerBuilder.withDebt(postDebt.getBody()).pay();
		ResponseEntity<Debt> payDebtFinal = debtControllerBuilder.withDebt(postDebt.getBody()).pay();

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertEquals(200, payDebt.getStatusCodeValue());
		assertEquals(200, payDebtFinal.getStatusCodeValue());
		assertEquals(2, debt.getInstallment());
		assertNotNull(payDebt.getBody());
		assertEquals(1, payDebt.getBody().getInstallment());
		assertNull(payDebtFinal.getBody());
	}
}