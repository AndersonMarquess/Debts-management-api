package com.andersonmarques.debts_api.services;

import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.repositories.DebtRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebtService {

	@Autowired
	private DebtRepository debtRepository;

	public Debt create(Debt debt) {
		return debtRepository.save(debt);
	}
}