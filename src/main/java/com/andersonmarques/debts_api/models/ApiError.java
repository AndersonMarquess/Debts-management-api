package com.andersonmarques.debts_api.models;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiError {
	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;

	public ApiError(HttpStatus status, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status.value();
		this.error = status.toString();
		this.message = message;
		this.path = path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}
}