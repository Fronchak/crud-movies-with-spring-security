package com.fronchak.locadora.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	private static final String EXISTING_USERNAME = "gabriel@gmail.com";
	private static final String NON_EXISTING_USERNAME = "invalid@gmail.com";
	
	@Mock
	private UserRepository repository;
	
	@InjectMocks
	private UserService service;
	
	@Test
	public void loadUserByUsernameShouldReturnUserWhenUsernameExists() {
		User entity = UserMocksFactory.mockUserEntity();
		when(repository.findByEmail(EXISTING_USERNAME)).thenReturn(entity);
		
		User result = (User) service.loadUserByUsername(EXISTING_USERNAME);
		assertEquals(30L, result.getId());
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertEquals("123456-0", result.getPassword());
		verify(repository, times(1)).findByEmail(EXISTING_USERNAME);
	}
	
	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
		when(repository.findByEmail(NON_EXISTING_USERNAME)).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(NON_EXISTING_USERNAME));
		verify(repository, times(1)).findByEmail(NON_EXISTING_USERNAME);
	}
}
