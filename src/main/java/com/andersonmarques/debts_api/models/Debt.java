package com.andersonmarques.debts_api.models;

import java.time.LocalDate;
import java.util.UUID;

public class Debt {
	private String id;
	private String description;
	private Double amount;
	private Integer installment;
	private Integer dueDay;
	private String ownerId;
	private LocalDate creationDate;

	public Debt() { 
		this.id = UUID.randomUUID().toString();
		this.creationDate = LocalDate.now();
		this.installment = 1;
	}

	public Debt(String description, Double amount, Integer installment, Integer dueDay, String ownerId) {
		this();
		this.description = description;
		this.amount = amount;
		this.installment = installment;
		this.dueDay = dueDay;
		this.ownerId = ownerId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getInstallment() {
		return this.installment;
	}

	public void setInstallment(Integer installment) {
		this.installment = installment;
	}

	public Integer getDueDay() {
		return this.dueDay;
	}

	public void setDueDay(Integer dueDay) {
		this.dueDay = dueDay;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void payMonthly() {
		this.installment--;
	}

	public Double getTotalAmount() {
		return this.amount * this.installment;
	}
}