package com.andersonmarques.debts_api.services;

import java.util.Optional;

import com.andersonmarques.debts_api.exceptions.InvalidDebtIdException;
import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.repositories.DebtRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtService {

	@Autowired
	private DebtRepository debtRepository;

	public Debt create(Debt debt) {
		return debtRepository.save(debt);
	}

	public Debt findById(String debtId) {
		Optional<Debt> debtOptional = debtRepository.findById(debtId);
		if (!debtOptional.isPresent()) {
			throw new InvalidDebtIdException("Dívida não encontrada, id inválido.");
		}
		return debtOptional.get();
	}

	public Debt pay(String debtId) {
		Debt debt = findById(debtId);
		if (debt.getInstallment() == 1) {
			delet(debtId);
			return null;
		}

		debt.payMonthly();
		return debtRepository.save(debt);
	}

	public void delet(String debtId) {
		debtRepository.deleteById(debtId);
	}

	public Page<Debt> findAllWithPg(Pageable pageable, String ownerId) {
		Debt debt = new Debt(null, null, null, null, ownerId);
		ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnorePaths("creationDate", "_id");
		return debtRepository.findAll(Example.of(debt, matcher), pageable);
	}
}