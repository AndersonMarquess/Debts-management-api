package com.andersonmarques.debts_api.models;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Debt {
	@Id
	private String id;
	@Size(min = 5, max = 60, message = "A descrição deve conter entre {min} e {max} caracteres.")
	private String description;
	@DecimalMin("1.0")
	@DecimalMax("999999.0")
	private Double amount;
	@Min(1)
	@Max(99)
	private Integer installment;
	private Integer dueDay;
	@NotEmpty(message = "O id do criador é obrigatório.")
	private String ownerId;
	private LocalDate creationDate;

	public Debt() {
		this.id = UUID.randomUUID().toString();
		this.creationDate = LocalDate.now();
		this.installment = 1;
		this.dueDay = 1;
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

	public LocalDate getDueDate() {
		return LocalDate.now().withDayOfMonth(dueDay);
	}
}