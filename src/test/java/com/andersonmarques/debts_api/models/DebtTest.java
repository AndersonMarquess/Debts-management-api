package com.andersonmarques.debts_api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class DebtTest {

	@Test
	public void verifyGettersAndSetters() {
		User user = new UserBuilder().build();
		Debt debt = new Debt();
		debt.setAmount(50d);
		debt.setDescription("description");
		debt.setDueDay(5);
		debt.setInstallment(10);
		debt.setOwnerId(user.getId());
		debt.setId("id");

		assertEquals(50d, debt.getAmount(), 0.0001d);
		assertEquals("description", debt.getDescription());
		assertEquals(5, debt.getDueDay());
		assertEquals(10, debt.getInstallment());
		assertEquals(user.getId(), debt.getOwnerId());
		assertEquals("id", debt.getId());
	}

	@Test
	public void updateInstallmentAfterPay() {
		Debt debt = new DebtBuilder().withAmount(50d).withInstallment(5).withDueDay(5).build();
		assertEquals(5, debt.getInstallment());
		assertEquals(250d, debt.getTotalAmount());
		debt.payMonthly();
		assertEquals(4, debt.getInstallment());
		assertEquals(200d, debt.getTotalAmount());
	}
}