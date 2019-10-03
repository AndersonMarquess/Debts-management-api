package com.andersonmarques.debts_api.controllers;

import javax.validation.Valid;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.services.DebtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebtController {

	private static final String APPLICATION_JSON = "application/json";
	private static final String BASE_PATH_V1 = "v1/debts";

	@Autowired
	private DebtService debtService;

	@PostMapping(path = BASE_PATH_V1, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<Debt> create(@Valid @RequestBody Debt debt) {
		debt = debtService.create(debt);
		System.out.println("Nova dívida criada: " + debt);
		return ResponseEntity.status(201).body(debt);
	}

	@PostMapping(path = BASE_PATH_V1 + "/pay/{debtId}")
	public ResponseEntity<Debt> pay(@PathVariable("debtId") String debtId) {
		Debt debt = debtService.pay(debtId);
		return ResponseEntity.ok().body(debt);
	}
}