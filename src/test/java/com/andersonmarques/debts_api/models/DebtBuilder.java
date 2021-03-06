package com.andersonmarques.debts_api.models;

import java.time.LocalDate;

public class DebtBuilder {

	private String description;
	private Double amount;
	private Integer installment;
	private Integer dueDay;
	private String ownerId;
	private boolean fixedCost;

	public DebtBuilder() {
		this.fixedCost = false;
		this.description = "dívida";
		this.amount = 10d;
		this.installment = 10;
		this.dueDay = 1;
		this.ownerId = new UserBuilder().build().getId();
	}

	public DebtBuilder withAmount(double amount) {
		this.amount = amount;
		return this;
	}

	public DebtBuilder withInstallment(int installment) {
		this.installment = installment;
		return this;
	}

	public DebtBuilder withDueDay(int dueDay) {
		this.dueDay = dueDay;
		return this;
	}

	public Debt build() {
		Debt debt = new Debt(description, amount, installment, LocalDate.now().withDayOfMonth(dueDay), ownerId);
		debt.setFixedCost(this.fixedCost);
		return debt;
	}

	public DebtBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public DebtBuilder withFixedCost() {
		this.fixedCost = true;
		this.installment = 1;
		return this;
	}
}
