package com.fronchak.locadora.mocks.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.role.RoleInputDTO;
import com.fronchak.locadora.dtos.role.RoleOutputDTO;
import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.dtos.user.UserUpdateDTO;
import com.fronchak.locadora.entities.Role;
import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.mocks.RoleMocksFactory;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class UserMocksFactoryTest {

	@Test
	public void mockUserEntityWithNoArgumentShouldMockValuesCorrectly() {
		User result = UserMocksFactory.mockUserEntity();
		Role role1 = RoleMocksFactory.mockRoleEntity(0);
		Role role2 = RoleMocksFactory.mockRoleEntity(1);
		assertEquals(30L, result.getId());
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertEquals("Encode123456-0", result.getPassword());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
	
	@Test
	public void mockUserEntityWithArgumentShouldMockValuesCorrectly() {
		User result = UserMocksFactory.mockUserEntity(1);
		Role role1 = RoleMocksFactory.mockRoleEntity(1);
		Role role2 = RoleMocksFactory.mockRoleEntity(2);
		assertEquals(31L, result.getId());
		assertEquals("mock_email_1@gmail.com.br", result.getEmail());
		assertEquals("Encode123456-1", result.getPassword());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
	
	@Test
	public void mockUserInsertDTOWithNoArgumentsShouldMockValuesCorrectly() {
		UserInsertDTO result = UserMocksFactory.mockUserInsertDTO();
		RoleInputDTO role1 = RoleMocksFactory.mockRoleInputDTO(0);
		RoleInputDTO role2 = RoleMocksFactory.mockRoleInputDTO(1);
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertEquals("Raw123456-0", result.getPassword());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
	
	@Test
	public void mockUserInsertDTOWithArgumentsShouldMockValuesCorrectly() {
		UserInsertDTO result = UserMocksFactory.mockUserInsertDTO(1);
		RoleInputDTO role1 = RoleMocksFactory.mockRoleInputDTO(1);
		RoleInputDTO role2 = RoleMocksFactory.mockRoleInputDTO(2);
		assertEquals("mock_email_1@gmail.com.br", result.getEmail());
		assertEquals("Raw123456-1", result.getPassword());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
	
	@Test
	public void mockUserUpdateDTOWithNoArgumentsShouldMockValuesCorrectly() {
		UserUpdateDTO result = UserMocksFactory.mockUserUpdateDTO();
		RoleInputDTO role1 = RoleMocksFactory.mockRoleInputDTO(0);
		RoleInputDTO role2 = RoleMocksFactory.mockRoleInputDTO(1);
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertEquals("Raw123456-0", result.getPassword());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
	
	@Test
	public void mockUserUpdateDTOWithArgumentsShouldMockValuesCorrectly() {
		UserUpdateDTO result = UserMocksFactory.mockUserUpdateDTO(1);
		RoleInputDTO role1 = RoleMocksFactory.mockRoleInputDTO(1);
		RoleInputDTO role2 = RoleMocksFactory.mockRoleInputDTO(2);
		assertEquals("mock_email_1@gmail.com.br", result.getEmail());
		assertEquals("Raw123456-1", result.getPassword());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
	
	@Test
	public void mockUserOutputDTOWithNoArgumentsShouldMockValuesCorrectly() {
		UserOutputDTO result = UserMocksFactory.mockUserOutputDTO();
		CustomizeAsserts.assertUserOutputDTO(result);
	}
	
	@Test
	public void mockUserOutputDTOWithArgumentsShouldMockValuesCorrectly() {
		UserOutputDTO result = UserMocksFactory.mockUserOutputDTO(1);
		RoleOutputDTO role1 = RoleMocksFactory.mockRoleOutputDTO(1);
		RoleOutputDTO role2 = RoleMocksFactory.mockRoleOutputDTO(2);
		assertEquals(31L, result.getId());
		assertEquals("mock_email_1@gmail.com.br", result.getEmail());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
}
