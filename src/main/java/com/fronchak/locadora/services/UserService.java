package com.fronchak.locadora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fronchak.locadora.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
}
