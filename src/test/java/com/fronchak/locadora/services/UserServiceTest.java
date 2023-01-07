package com.fronchak.locadora.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	private static final String EXISTING_USERNAME = "gabriel@gmail.com";
	private static final String NON_EXISTING_USERNAME = "invalid@gmail.com";
	
	@Mock
	private UserRepository repository;
	
	@InjectMocks
	private UserService service;
}
