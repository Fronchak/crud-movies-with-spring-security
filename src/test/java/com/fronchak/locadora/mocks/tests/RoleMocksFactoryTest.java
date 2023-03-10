package com.fronchak.locadora.mocks.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.role.RoleInputDTO;
import com.fronchak.locadora.dtos.role.RoleOutputDTO;
import com.fronchak.locadora.entities.Role;
import com.fronchak.locadora.mocks.RoleMocksFactory;

@ExtendWith(SpringExtension.class)
public class RoleMocksFactoryTest {

	@Test
	public void mockEntityPassingNoArgumentsShouldMockValuesCorrectly() {
		Role result = RoleMocksFactory.mockRoleEntity();
		assertEquals(20L, result.getId());
		assertEquals("ROLE_AUTHORITY_0", result.getAuthority());
	}
	
	@Test
	public void mockEntityPassingArgumentsShouldMockValuesCorrectly() {
		Role result = RoleMocksFactory.mockRoleEntity(1);
		assertEquals(21L, result.getId());
		assertEquals("ROLE_AUTHORITY_1", result.getAuthority());
	}
	
	@Test
	public void mockOutputDTOPassingNoArgumentsShouldMockValuesCorrectly() {
		RoleOutputDTO result = RoleMocksFactory.mockRoleOutputDTO();
		assertEquals(20L, result.getId());
		assertEquals("ROLE_AUTHORITY_0", result.getAuthority());
	}
	
	@Test
	public void mockOutputDTOPassingArgumentsShouldMockValuesCorrectly() {
		RoleOutputDTO result = RoleMocksFactory.mockRoleOutputDTO(1);
		assertEquals(21L, result.getId());
		assertEquals("ROLE_AUTHORITY_1", result.getAuthority());
	}
	
	@Test
	public void mockInputDTOPassingNoArgumentsShouldMockValuesCorrectly() {
		RoleInputDTO result = RoleMocksFactory.mockRoleInputDTO();
		assertEquals(20L, result.getId());
	}
	
	@Test
	public void mockInputDTOPassingArgumentsShouldMockValuesCorrectly() {
		RoleInputDTO result = RoleMocksFactory.mockRoleInputDTO(1);
		assertEquals(21L, result.getId());
	}
}
