package com.andersonmarques.debts_api.controllers;

import javax.validation.Valid;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.services.DebtService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebtController {

	private static final Logger logger = LogManager.getLogger("DebtController");
	private static final String APPLICATION_JSON = "application/json";
	private static final String BASE_PATH_V1 = "v1/debts";

	@Autowired
	private DebtService debtService;

	@PostMapping(path = BASE_PATH_V1, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<Debt> create(@Valid @RequestBody Debt debt) {
		logger.info("Tentando criar nova dívida: {}", debt);
		debt = debtService.create(debt);
		logger.info("Nova dívida criada com sucesso: {}", debt);
		return ResponseEntity.status(201).body(debt);
	}

	@PostMapping(path = BASE_PATH_V1 + "/pay/{debtId}")
	public ResponseEntity<Debt> pay(@PathVariable("debtId") String debtId) {
		logger.info("Tentando pagar dívida com id: {}", debtId);
		Debt debt = debtService.pay(debtId);
		logger.info("Dívida paga com sucesso: {}", debt);
		return ResponseEntity.ok().body(debt);
	}

	@GetMapping(path = BASE_PATH_V1, produces = APPLICATION_JSON)
	public ResponseEntity<Page<Debt>> listAllWithPg(Pageable pageable, @RequestHeader("userId") String userId) {
		logger.info("Tentando listar todas as dívidas do usuário com id: {}", userId);
		Page<Debt> debts = debtService.findAllWithPg(pageable, userId);
		logger.info("Retornando listar com {} dívidas e {} páginas", debts.getNumberOfElements(),
				debts.getTotalPages());
		return ResponseEntity.ok().body(debts);
	}

	@GetMapping(path = BASE_PATH_V1 + "/{debtId}", produces = APPLICATION_JSON)
	public ResponseEntity<Debt> findDetails(@PathVariable("debtId") String debtId) {
		logger.info("Tentando buscar dívida com id: {}", debtId);
		Debt debt = debtService.findById(debtId);
		logger.info("Dívida encontrada com sucesso: {}", debt);
		return ResponseEntity.ok().body(debt);
	}

	@DeleteMapping(path = BASE_PATH_V1 + "/{debtId}", produces = APPLICATION_JSON)
	public ResponseEntity<Void> deleteById(@PathVariable("debtId") String debtId) {
		logger.info("Tentando remover dívida com id: {}", debtId);
		debtService.deleteById(debtId);
		logger.info("Dívida com id {} removida com sucesso", debtId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = BASE_PATH_V1, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<Debt> update(@Valid @RequestBody Debt debt) {
		logger.info("Tentando atualizar dívida: {}", debt);
		debt = debtService.update(debt);
		logger.info("Dívida atualizada com sucesso: {}", debt);
		return ResponseEntity.ok().body(debt);
	}
}