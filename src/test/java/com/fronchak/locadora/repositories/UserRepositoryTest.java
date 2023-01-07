package com.fronchak.locadora.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fronchak.locadora.entities.User;

@DataJpaTest
public class UserRepositoryTest {

	private static final String EXISTING_EMAIL = "gabriel@gmail.com";
	private static final String NON_EXISTING_EMAIL = "invalid@gmail.com";
	
	@Autowired
	private UserRepository repository;
	
	@Test
	public void findByEmailShouldReturnUserWhenEmailExists() {
		User result = repository.findByEmail(EXISTING_EMAIL);
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(EXISTING_EMAIL, result.getEmail());
	}
	
	@Test
	public void findByEmailShouldReturnNullWhenEmailDoesNotExist() {
		User result = repository.findByEmail(NON_EXISTING_EMAIL);
		assertNull(result);
	}
}
