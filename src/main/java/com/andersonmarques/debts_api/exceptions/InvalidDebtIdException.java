package com.andersonmarques.debts_api.exceptions;

public class InvalidDebtIdException extends RuntimeException {
	private static final long serialVersionUID = -660235621880286286L;

	public InvalidDebtIdException(String message) {
		super(message);
	}
}