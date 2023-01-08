package com.fronchak.locadora.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.dtos.user.UserUpdateDTO;
import com.fronchak.locadora.entities.Role;
import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.exceptions.DatabaseException;
import com.fronchak.locadora.exceptions.InvalidPasswordException;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mappers.UserMapper;
import com.fronchak.locadora.mocks.RoleMocksFactory;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.repositories.RoleRepository;
import com.fronchak.locadora.repositories.UserRepository;
import com.fronchak.locadora.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	private static final String EXISTING_USERNAME = "gabriel@gmail.com";
	private static final String NON_EXISTING_USERNAME = "invalid@gmail.com";
	
	private static final Long EXISTING_ID = 1L;
	private static final Long NON_EXISTING_ID = 2L;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private UserMapper mapper;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserService service;
	
	@Test
	public void loadUserByUsernameShouldReturnUserWhenUsernameExists() {
		User entity = UserMocksFactory.mockUserEntity();
		when(repository.findByEmail(EXISTING_USERNAME)).thenReturn(entity);
		
		User result = (User) service.loadUserByUsername(EXISTING_USERNAME);
		assertEquals(30L, result.getId());
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertEquals("Encode123456-0", result.getPassword());
		verify(repository, times(1)).findByEmail(EXISTING_USERNAME);
	}
	
	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
		when(repository.findByEmail(NON_EXISTING_USERNAME)).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(NON_EXISTING_USERNAME));
		verify(repository, times(1)).findByEmail(NON_EXISTING_USERNAME);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> service.findById(NON_EXISTING_ID));
		verify(repository, times(1)).findById(NON_EXISTING_ID);
	}
	
	@Test
	public void findByIdShouldReturnOutputDTOWhenIdExists() {
		User entity = UserMocksFactory.mockUserEntity();
		UserOutputDTO outputDTO = UserMocksFactory.mockUserOutputDTO();
		
		when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(entity));
		when(mapper.convertEntityToOutputDTO(entity)).thenReturn(outputDTO);
		
		UserOutputDTO result = service.findById(EXISTING_ID);
		
		CustomizeAsserts.assertUserOutputDTO(result);
		verify(repository, times(1)).findById(EXISTING_ID);
	}
	
	@Test
	public void saveShouldReturnOutputDTOAfterSaveEntity() {
		UserInsertDTO insertDTO = UserMocksFactory.mockUserInsertDTO();
		User entity = UserMocksFactory.mockUserEntity();
		UserOutputDTO outputDTO = UserMocksFactory.mockUserOutputDTO();
		String passwordEncoded = "passwordEncoded";
		Role role1 = RoleMocksFactory.mockRoleEntity(0);
		Role role2 = RoleMocksFactory.mockRoleEntity(1);
		ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
		
		when(repository.save(any(User.class))).thenReturn(entity);
		when(mapper.convertEntityToOutputDTO(entity)).thenReturn(outputDTO);
		when(passwordEncoder.encode(insertDTO.getPassword())).thenReturn(passwordEncoded);
		when(roleRepository.getReferenceById(20L)).thenReturn(role1);
		when(roleRepository.getReferenceById(21L)).thenReturn(role2);
		
		UserOutputDTO result = service.save(insertDTO);
		verify(repository).save(argumentCaptor.capture());
		User userResult = argumentCaptor.getValue();
		
		verify(roleRepository, times(1)).getReferenceById(20L);
		verify(roleRepository, times(1)).getReferenceById(21L);
		verify(mapper, times(1)).copyDTOToEntity(insertDTO, userResult);
		verify(passwordEncoder, times(1)).encode(insertDTO.getPassword());
		assertEquals(passwordEncoded, userResult.getPassword());
		assertTrue(userResult.getRoles().contains(role1));
		assertTrue(userResult.getRoles().contains(role2));
		
		CustomizeAsserts.assertUserOutputDTO(result);
	}
	
	@Test
	public void saveShouldThrowDatabaseExceptionWhenRoleIdDoesNotExist() {
		UserInsertDTO insertDTO = UserMocksFactory.mockUserInsertDTO();
		
		when(roleRepository.getReferenceById(20L)).thenThrow(DataIntegrityViolationException.class);
		
		assertThrows(DatabaseException.class, () -> service.save(insertDTO));
		verify(repository, never()).save(any());
		verify(roleRepository, times(1)).getReferenceById(20L);
	}
	
	@Test
	public void updateShouldThrowInvalidPasswordExceptionWhenPasswordIsInvalid() {
		UserUpdateDTO updateDTO = UserMocksFactory.mockUserUpdateDTO();
		User entity = UserMocksFactory.mockUserEntity();
		
		when(repository.getReferenceById(EXISTING_ID)).thenReturn(entity);
		when(passwordEncoder.matches(updateDTO.getOldPassword(), entity.getPassword())).thenReturn(false);
		
		assertThrows(InvalidPasswordException.class, () -> service.update(updateDTO, EXISTING_ID));
		verify(repository, never()).save(any());
		verify(repository, times(1)).getReferenceById(EXISTING_ID);
		verify(passwordEncoder, times(1)).matches(updateDTO.getOldPassword(), entity.getPassword());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		UserUpdateDTO updateDTO = UserMocksFactory.mockUserUpdateDTO();
		
		when(repository.getReferenceById(NON_EXISTING_ID)).thenThrow(EntityNotFoundException.class);
		
		assertThrows(ResourceNotFoundException.class, () -> service.update(updateDTO, NON_EXISTING_ID));
		verify(repository, never()).save(any());
		verify(repository, times(1)).getReferenceById(NON_EXISTING_ID);
	}
	
	@Test
	public void updateShouldThrowDatabaseExceptionWhenRoleIdDoesNotExist() {
		UserUpdateDTO updateDTO = UserMocksFactory.mockUserUpdateDTO();
		User entity = UserMocksFactory.mockUserEntity();
		
		when(repository.getReferenceById(EXISTING_ID)).thenReturn(entity);
		when(roleRepository.getReferenceById(20L)).thenThrow(DataIntegrityViolationException.class);
		when(passwordEncoder.matches(updateDTO.getOldPassword(), entity.getPassword())).thenReturn(true);
		
		assertThrows(DatabaseException.class, () -> service.update(updateDTO, EXISTING_ID));
		verify(repository, times(1)).getReferenceById(EXISTING_ID);
		verify(roleRepository, times(1)).getReferenceById(20L);
		verify(repository, never()).save(any());
		verify(passwordEncoder, times(1)).matches(updateDTO.getOldPassword(), entity.getPassword());
	}
	
	@Test
	public void updateShouldReturnOutputDTOWhenPasswordsMatch() {
		UserUpdateDTO updateDTO = UserMocksFactory.mockUserUpdateDTO();
		UserOutputDTO outputDTO = UserMocksFactory.mockUserOutputDTO();
		User newEntity = UserMocksFactory.mockUserEntity(0);
		User oldEntity = UserMocksFactory.mockUserEntity(1);
		Role role1 = RoleMocksFactory.mockRoleEntity(0);
		Role role2 = RoleMocksFactory.mockRoleEntity(1);
		ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
		String oldPassword = oldEntity.getPassword();
		String newPassword = "newPassword";
		
		when(repository.getReferenceById(EXISTING_ID)).thenReturn(oldEntity);
		when(passwordEncoder.matches(updateDTO.getOldPassword(), oldEntity.getPassword())).thenReturn(true);
		when(passwordEncoder.encode(updateDTO.getPassword())).thenReturn(newPassword);
		when(roleRepository.getReferenceById(20L)).thenReturn(role1);
		when(roleRepository.getReferenceById(21L)).thenReturn(role2);
		when(repository.save(oldEntity)).thenReturn(newEntity);
		when(mapper.convertEntityToOutputDTO(newEntity)).thenReturn(outputDTO);
		
		UserOutputDTO result = service.update(updateDTO, EXISTING_ID);
		verify(repository).save(argumentCaptor.capture());
		User userResult = argumentCaptor.getValue();
		
		verify(repository, times(1)).getReferenceById(EXISTING_ID);
		verify(passwordEncoder, times(1)).matches(updateDTO.getOldPassword(), oldPassword);
		verify(passwordEncoder, times(1)).encode(updateDTO.getPassword());
		verify(roleRepository, times(1)).getReferenceById(20L);
		verify(roleRepository, times(1)).getReferenceById(21L);
		verify(repository, times(1)).save(oldEntity);
		verify(mapper, times(1)).convertEntityToOutputDTO(newEntity);
		verify(mapper, times(1)).copyDTOToEntity(updateDTO, oldEntity);
		
		assertEquals(newPassword, userResult.getPassword());
		assertTrue(userResult.getRoles().contains(role1));
		assertTrue(userResult.getRoles().contains(role1));
		assertEquals(2, userResult.getRoles().size());
		CustomizeAsserts.assertUserOutputDTO(result);
	}
}
