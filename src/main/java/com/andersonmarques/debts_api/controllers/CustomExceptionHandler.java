package com.andersonmarques.debts_api.controllers;

import javax.servlet.http.HttpServletRequest;

import com.andersonmarques.debts_api.models.ApiError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> beanValidationError(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		String message = exception.getBindingResult().getFieldError().getDefaultMessage();
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
		return ResponseEntity.status(error.getStatus()).body(error);
	}
}