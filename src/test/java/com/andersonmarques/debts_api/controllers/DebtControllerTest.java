package com.andersonmarques.debts_api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.models.DebtBuilder;
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
public class DebtControllerTest {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TestRestTemplate client;
	private HttpHeaders headers;
	private DebtControllerBuilder debtControllerBuilder;
	private UserControllerBuilder userControllerBuilder;
	private static final String AUTHORIZATION = "Authorization";

	@BeforeEach
	public void setupObjects() {
		mongoTemplate.getDb().drop();
		headers = new HttpHeaders();
		headers.add("content-Type", "application/json");
		headers.add("accept", "application/json");
		this.debtControllerBuilder = new DebtControllerBuilder(client, headers);
		this.userControllerBuilder = new UserControllerBuilder(client, headers);
	}

	private void setUserTokenInHeader(User user) {
		String jwt = userControllerBuilder.login(user.getEmail(), "password").getHeaders().getFirst(AUTHORIZATION);
		headers.remove(AUTHORIZATION);
		headers.add(AUTHORIZATION, jwt);
	}

	@Test
	public void notAllowCreateDebtWithoutAValidUser() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		Debt debt = new DebtBuilder().withAmount(25d).withInstallment(3).build();
		ResponseEntity<Debt> postDebt = debtControllerBuilder.withDebt(debt).post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertNotNull(postDebt.getBody());
		assertEquals(25d, postDebt.getBody().getAmount());
		assertEquals(debt.getTotalAmount(), postDebt.getBody().getTotalAmount());
		assertEquals(postUser.getBody().getId(), postDebt.getBody().getOwnerId());
	}

	@Test
	public void notAllowCreateDebtWithZeroOrNegativeAmount() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		Debt debt = new DebtBuilder().withAmount(-25d).build();
		ResponseEntity<Debt> postDebt = debtControllerBuilder.withDebt(debt).post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(400, postDebt.getStatusCodeValue());
	}

	@Test
	public void notAllowCreateDebtWithZeroOrNegativeInstallment() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		Debt debt = new DebtBuilder().withInstallment(-1).build();
		ResponseEntity<Debt> postDebt = debtControllerBuilder.withDebt(debt).post(postUser.getBody());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(400, postDebt.getStatusCodeValue());
	}

	@Test
	public void reduceInstallmentAfterPay() {
		Debt debt = new DebtBuilder().withInstallment(10).build();
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
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
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
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

	@Test
	public void sendUserRefInHeaderForListAllDebtOfUser() {
		ResponseEntity<User> user = userControllerBuilder.post();
		setUserTokenInHeader(user.getBody());
		debtControllerBuilder.postMultiples(5, user.getBody().getId());
		ResponseEntity<String> debts = debtControllerBuilder.findAllPageable(user.getBody().getId());

		assertEquals(201, user.getStatusCodeValue());
		assertEquals(200, debts.getStatusCodeValue());
		assertNotNull(debts.getBody());
	}

	@Test
	public void getDetailsOfSpecifiedDebtById() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		ResponseEntity<Debt> postDebt = debtControllerBuilder.post(postUser.getBody());
		Debt debt = postDebt.getBody();
		ResponseEntity<Debt> postDebt2 = debtControllerBuilder.post(postUser.getBody());
		ResponseEntity<Debt> getDebt = debtControllerBuilder.getDetailsFor(debt.getId());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertEquals(201, postDebt2.getStatusCodeValue());
		assertEquals(200, getDebt.getStatusCodeValue());
		assertNotNull(postDebt.getBody());
		assertEquals(debt.getDescription(), getDebt.getBody().getDescription());
		assertEquals(debt.getAmount(), getDebt.getBody().getAmount());
	}

	@Test
	public void removeDebtById() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		ResponseEntity<Debt> postDebt = debtControllerBuilder.post(postUser.getBody());
		String debtId = postDebt.getBody().getId();
		ResponseEntity<Void> deleteDebt = debtControllerBuilder.delete(debtId);
		ResponseEntity<Debt> getDebt = debtControllerBuilder.getDetailsFor(debtId);

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertEquals(200, deleteDebt.getStatusCodeValue());
		assertEquals(400, getDebt.getStatusCodeValue());
	}

	@Test
	public void updateDebtDetails() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		ResponseEntity<Debt> postDebt = debtControllerBuilder.post(postUser.getBody());
		Debt debt = postDebt.getBody();
		debt.setAmount(200d);
		debt.setInstallment(48);
		ResponseEntity<Debt> updateDebt = debtControllerBuilder.updateDebt(debt);
		ResponseEntity<Debt> getDebt = debtControllerBuilder.getDetailsFor(debt.getId());

		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertEquals(200, getDebt.getStatusCodeValue());
		assertEquals(200, updateDebt.getStatusCodeValue());
		assertEquals(debt.getAmount(), getDebt.getBody().getAmount());
		assertEquals(debt.getInstallment(), getDebt.getBody().getInstallment());
	}

	@Test
	public void notAllowUserToModifyDebtsNotCreatedForHimself() {
		ResponseEntity<User> postUser = userControllerBuilder.post();
		setUserTokenInHeader(postUser.getBody());
		ResponseEntity<Debt> postDebt = debtControllerBuilder.post(postUser.getBody());
		Debt debt = postDebt.getBody();
		debt.setInstallment(99);
		
		User user = new UserBuilder().withEmail("user02@email.com").build();
		ResponseEntity<User> postUser2 = userControllerBuilder.withUser(user).post();
		setUserTokenInHeader(postUser2.getBody());
		ResponseEntity<Debt> updateDebt = debtControllerBuilder.updateDebt(debt);
		
		assertEquals(201, postUser.getStatusCodeValue());
		assertEquals(201, postDebt.getStatusCodeValue());
		assertEquals(403, updateDebt.getStatusCodeValue());
	}
}