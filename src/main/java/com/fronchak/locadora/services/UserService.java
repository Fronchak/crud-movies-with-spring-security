package com.fronchak.locadora.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.locadora.dtos.user.UserInputDTO;
import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.dtos.user.UserUpdateDTO;
import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.exceptions.DatabaseException;
import com.fronchak.locadora.exceptions.InvalidPasswordException;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mappers.UserMapper;
import com.fronchak.locadora.repositories.RoleRepository;
import com.fronchak.locadora.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public UserOutputDTO findById(Long id) {
		User entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
		return mapper.convertEntityToOutputDTO(entity);
	}
	
	@Transactional
	public UserOutputDTO save(UserInsertDTO insertDTO) {
		try {
			User entity = new User();
			copyDTOToEntity(insertDTO, entity);
			entity = repository.save(entity);
			return mapper.convertEntityToOutputDTO(entity);		
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Invalid role id");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Invalid role ID");
		}
	}
	
	private void copyDTOToEntity(UserInputDTO dto, User entity) {
		mapper.copyDTOToEntity(dto, entity);
		entity.getRoles().clear();
		dto.getRoles().forEach(role -> entity.addRole(roleRepository.getReferenceById(role.getId())));
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
	}
	
	@Transactional
	public UserOutputDTO update(UserUpdateDTO updateDTO, Long id) {
		try {
			User entity = repository.getReferenceById(id);
			if(isPasswordInvalid(updateDTO, entity)) {
				throw new InvalidPasswordException("Invalid password, please try again");
			}
			copyDTOToEntity(updateDTO, entity);
			entity = repository.save(entity);
			return mapper.convertEntityToOutputDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("User", id.toString());
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Invalid role ID");
		}
	}
	
	private boolean isPasswordInvalid(UserUpdateDTO updateDTO, User entity) {
		return !isPasswordValid(updateDTO, entity);
	}
	
	private boolean isPasswordValid(UserUpdateDTO updateDTO, User entity) {
		return passwordEncoder.matches(updateDTO.getOldPassword(), entity.getPassword());
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);			
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("User", id.toString());
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("User cannot be deleted");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User entity = repository.findByEmail(username);
		if(entity == null) {
			throw new UsernameNotFoundException("Username not found: " + username);
		}
		return entity;
	}
}
