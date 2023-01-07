package com.fronchak.locadora.mocks.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.mocks.UserMocksFactory;

@ExtendWith(SpringExtension.class)
public class UserMocksFactoryTest {

	@Test
	public void mockUserEntityWithNoArgumentShouldMockValuesCorrectly() {
		User result = UserMocksFactory.mockUserEntity();
		assertEquals(30L, result.getId());
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertEquals("123456-0", result.getPassword());
	}
	
	@Test
	public void mockUserEntityWithArgumentShouldMockValuesCorrectly() {
		User result = UserMocksFactory.mockUserEntity(1);
		assertEquals(31L, result.getId());
		assertEquals("mock_email_1@gmail.com.br", result.getEmail());
		assertEquals("123456-1", result.getPassword());
	}
}
