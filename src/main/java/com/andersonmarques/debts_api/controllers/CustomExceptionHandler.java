package com.andersonmarques.debts_api.controllers;

import javax.servlet.http.HttpServletRequest;

import com.andersonmarques.debts_api.exceptions.InvalidDebtIdException;
import com.andersonmarques.debts_api.models.ApiError;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	private static final Logger logger = LogManager.getLogger("CustomExceptionHandler");

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> beanValidationError(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		String message = exception.getBindingResult().getFieldError().getDefaultMessage();
		logger.info("Request para {} resutou em uma exceção de validação: {}", request.getRequestURI(), message);
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
		return ResponseEntity.status(error.getStatus()).body(error);
	}

	@ExceptionHandler(InvalidDebtIdException.class)
	public ResponseEntity<ApiError> invalidDebtId(InvalidDebtIdException exception, HttpServletRequest request) {
		logger.info("Request para {} resutou em uma exceção: {}", request.getRequestURI(), exception.getMessage());
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(error.getStatus()).body(error);
	}
}