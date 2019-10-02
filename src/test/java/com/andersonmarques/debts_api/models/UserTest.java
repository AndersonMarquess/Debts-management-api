package com.andersonmarques.debts_api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootTest
public class UserTest {

	@Test
	public void verifyGettersAndSetter() {
		User user = new User();
		user.setId("id");
		user.setName("nome");
		user.setEmail("email@email.com");
		user.setPassword("password");

		assertEquals("id", user.getId());
		assertEquals("nome", user.getName());
		assertEquals("email@email.com", user.getEmail());
		assertNotEquals("password", user.getPassword());
		assertTrue(BCrypt.checkpw("password", user.getPassword()));
	}

	@Test
	public void avoidDuplicatedRoles() {
		User user = new UserBuilder().withRole("user").build();
		user.addRole("user");
		assertNotNull(user);
		assertTrue(user.getRoles().size() == 1);
		assertTrue(user.getRoles().contains(new SimpleGrantedAuthority("USER")));
	}

	@Test
	public void verifyHashedPasswordWithBcrypt() {
		User user = new UserBuilder().withPassword("super_seguro").build();
		assertTrue(BCrypt.checkpw("super_seguro", user.getPassword()));
	}

	@Test
	public void changeRolesOutsideClassThrowsException() {
		User user = new UserBuilder().build();
		Optional<SimpleGrantedAuthority> role = user.getRoles().stream().findFirst();
		assertThrows(UnsupportedOperationException.class, () -> user.getRoles().remove(role.get()));
		assertEquals(1, user.getRoles().size());
	}
}