package com.andersonmarques.debts_api.controllers;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.models.DebtBuilder;
import com.andersonmarques.debts_api.models.User;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class DebtControllerBuilder {
	private TestRestTemplate client;
	private HttpHeaders headers;
	private Debt debt;

	public DebtControllerBuilder(TestRestTemplate client, HttpHeaders headers) {
		this.client = client;
		this.headers = headers;
		this.debt = new DebtBuilder().build();
	}

	/**
	 * Must be a valid user, one already persisted, if necessery use
	 * {@link UserControllerBuilder}.
	 * 
	 * @param user
	 * @return
	 */
	public ResponseEntity<Debt> post(User user) {
		this.debt.setOwnerId(user.getId());
		return client.postForEntity("/v1/debts", new HttpEntity<Debt>(debt, headers), Debt.class);
	}

	public void postMultiples(int quantity, String userId) {
		this.debt.setOwnerId(userId);
		for (int i = 0; i < quantity; i++) {
			this.debt.setId(i + " ID");
			this.debt.setDescription("dívida nª " + i);
			client.postForEntity("/v1/debts", new HttpEntity<Debt>(debt, headers), Debt.class);
		}
	}

	public DebtControllerBuilder withDebt(Debt debt) {
		this.debt = debt;
		return this;
	}

	public ResponseEntity<Debt> pay() {
		return client.postForEntity("/v1/debts/pay/" + this.debt.getId(), null, Debt.class);
	}

	public ResponseEntity<String> findAllPageable(String userId) {
		headers.add("userId", userId);
		return client.exchange("/v1/debts?page=0&size=50", HttpMethod.GET, new HttpEntity<>(headers), String.class);
	}

	public ResponseEntity<Debt> getDetailsFor(String debtId) {
		return client.exchange("/v1/debts/" + debtId, HttpMethod.GET, new HttpEntity<>(headers), Debt.class);
	}

	public ResponseEntity<Void> delete(String debtId) {
		return client.exchange("/v1/debts/" + debtId, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
	}
}
