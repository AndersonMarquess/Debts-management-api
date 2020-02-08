package com.andersonmarques.debts_api.services;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import java.util.Optional;

import com.andersonmarques.debts_api.exceptions.InvalidDebtIdException;
import com.andersonmarques.debts_api.models.Debt;
import com.andersonmarques.debts_api.repositories.DebtRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtService {

	private static final Logger logger = LogManager.getLogger("DebtService");

	@Autowired
	private DebtRepository debtRepository;
	@Autowired
	private UserService userService;

	public Debt create(Debt debt) {
		debt.setOwnerId(userService.getAuthenticatedUserId());
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
		if (debt.getCurrentInstallment() == debt.getTotalInstallment() && !debt.isFixedCost()) {
			logger.info("Pagamento da última prestação da dívida {}", debt);
			deleteById(debtId);
			return null;
		}

		debt.payMonthly();
		return debtRepository.save(debt);
	}

	public void deleteById(String debtId) {
		debtRepository.deleteById(debtId);
	}

	public Page<Debt> findAllWithPg(Pageable pageable, String ownerId) {
		Debt debt = new Debt(null, null, null, null, ownerId);
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("ownerId", exact())
				.withIgnorePaths(getAttributesToIgnoreInDebt());
		
		return debtRepository.findAll(Example.of(debt, matcher), pageable);
	}

	private String[] getAttributesToIgnoreInDebt() {
		String ignoreAttributes[] = { "id", "description", "category", "amount", "currentInstallment",
				"totalInstallment", "dueDate", "fixedCost" };
		return ignoreAttributes;
	}

	public Debt update(Debt debt) {
		return debtRepository.save(debt);
	}
}