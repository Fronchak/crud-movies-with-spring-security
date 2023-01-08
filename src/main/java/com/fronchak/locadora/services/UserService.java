package com.fronchak.locadora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.locadora.dtos.user.UserInputDTO;
import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.exceptions.DatabaseException;
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
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Invalid role ID");
		}
	}
	
	private void copyDTOToEntity(UserInputDTO dto, User entity) {
		mapper.copyDTOToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity.getRoles().clear();
		dto.getRoles().forEach(role -> entity.addRole(roleRepository.getReferenceById(role.getId())));
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
