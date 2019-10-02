package com.andersonmarques.debts_api.models;

public class DebtBuilder {

	private String description;
	private Double amount;
	private Integer installment;
	private Integer dueDay;
	private String ownerId;

	public DebtBuilder() {
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
		return new Debt(description, amount, installment, dueDay, ownerId);
	}
}
