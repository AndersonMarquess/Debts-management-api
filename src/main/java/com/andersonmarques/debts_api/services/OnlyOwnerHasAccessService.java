package com.andersonmarques.debts_api.services;

import com.andersonmarques.debts_api.exceptions.IllegalAccessToModificationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OnlyOwnerHasAccessService {

	@Autowired
	private UserService userService;
	@Autowired
	private DebtService debtService;

	public void verify(String debtId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String userId = userService.findByEmail(auth.getName()).get().getId();
		boolean ownerMatch = debtService.findById(debtId).getOwnerId().equals(userId);
		
		if (!ownerMatch) {
			throw new IllegalAccessToModificationException("Você não tem permissão para modificar esta dívida");
		}
	}
}
