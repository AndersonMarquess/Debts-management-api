package com.andersonmarques.debts_api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

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
		debt.setCategory("category");
		debt.setDueDate(LocalDate.of(2019, 02, 15));
		debt.setTotalInstallment(10);
		debt.setOwnerId(user.getId());
		debt.setId("id");

		assertEquals(50d, debt.getAmount(), 0.0001d);
		assertEquals("description", debt.getDescription());
		assertEquals("category", debt.getCategory());
		assertEquals(LocalDate.of(2019, 02, 15), debt.getDueDate());
		assertEquals(1, debt.getCurrentInstallment());
		assertEquals(10, debt.getTotalInstallment());
		assertEquals(user.getId(), debt.getOwnerId());
		assertEquals("id", debt.getId());
	}

	@Test
	public void updateInstallmentAfterPay() {
		LocalDate dateWithChoosenDay = LocalDate.now().withDayOfMonth(5);
		Debt debt = new DebtBuilder().withAmount(50d).withInstallment(5).withDueDay(5).build();
		Double amountBaseControl = 300d;

		for (int i = 0; i < 6; i++) {
			amountBaseControl -= 50d;
			assertEquals(i + 1, debt.getCurrentInstallment());
			assertEquals(amountBaseControl, debt.getTotalAmountLeft(), 0.0001d);
			assertEquals(dateWithChoosenDay.getMonth().plus(i), debt.getDueDate().getMonth());
			debt.payMonthly();
		}
	}
}