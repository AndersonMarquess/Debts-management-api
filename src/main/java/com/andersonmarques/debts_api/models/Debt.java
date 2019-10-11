package com.andersonmarques.debts_api.models;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Debt {
	@Id
	private String id;
	@Size(min = 5, max = 60, message = "A descrição deve conter entre {min} e {max} caracteres.")
	private String description;
	private String category;
	@DecimalMin("1.0")
	@DecimalMax("999999.0")
	private Double amount;
	private Integer currentInstallment;
	@Min(1)
	@Max(99)
	private Integer totalInstallment;
	private String ownerId;
	private LocalDate dueDate;

	public Debt() {
		this.id = UUID.randomUUID().toString();
		this.dueDate = LocalDate.now();
		this.currentInstallment = 1;
		this.totalInstallment = 1;
	}

	public Debt(String description, Double amount, Integer totalInstallment, LocalDate dueDate, String ownerId) {
		this();
		this.description = description;
		this.amount = amount;
		this.totalInstallment = totalInstallment;
		this.dueDate = dueDate;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCurrentInstallment() {
		return currentInstallment;
	}

	public void setCurrentInstallment(Integer currentInstallment) {
		this.currentInstallment = currentInstallment;
	}

	public Integer getTotalInstallment() {
		return totalInstallment;
	}

	public void setTotalInstallment(Integer totalInstallment) {
		this.totalInstallment = totalInstallment;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void payMonthly() {
		this.currentInstallment++;
		this.dueDate.plusMonths(1);
	}

	public Double getTotalAmount() {
		return this.amount * this.totalInstallment;
	}

	public Double getTotalAmountLeft() {
		if (this.currentInstallment == 1) {
			return getTotalAmount();
		}

		return getAmount() * (getTotalInstallment() - (getCurrentInstallment() - 1));
	}

	public LocalDate getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", description='" + getDescription() + "'" +
			", category='" + getCategory() + "'" +
			", amount='" + getAmount() + "'" +
			", currentInstallment='" + getCurrentInstallment() + "'" +
			", totalInstallment='" + getTotalInstallment() + "'" +
			", dueDate='" + getDueDate() + "'" +
			", ownerId='" + getOwnerId() + "'" +
			"}";
	}
}