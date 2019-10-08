package com.andersonmarques.debts_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalAccessToModificationException extends RuntimeException {
	private static final long serialVersionUID = 5129180987971693335L;

	public IllegalAccessToModificationException(String msg) {
		super(msg);
	}
}