package com.andersonmarques.debts_api.controllers;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.models.DebtBuilder;
import com.andersonmarques.debts_api.models.User;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

	public DebtControllerBuilder withDebt(Debt debt) {
		this.debt = debt;
		return this;
	}
}
