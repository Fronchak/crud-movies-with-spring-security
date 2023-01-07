package com.fronchak.locadora.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.mocks.RoleMocksFactory;

@ExtendWith(SpringExtension.class)
public class UserTest {

	@Test
	public void addRoleShouldAddRoleToRolesList() {
		User user = new User();
		Role role = RoleMocksFactory.mockRoleEntity();
		user.addRole(role);
		
		Role expected = RoleMocksFactory.mockRoleEntity();
		assertTrue(user.getRoles().contains(expected));
		assertEquals(1, user.getRoles().size());
	}
	
	@Test
	public void addRoleShouldAddRoleToTheListWhenRolesListDoesNotContainRole() {
		User user = new User();
		Role role1 = RoleMocksFactory.mockRoleEntity(1);
		Role role2 = RoleMocksFactory.mockRoleEntity(2);
		Role expected1 = RoleMocksFactory.mockRoleEntity(1);
		Role expected2 = RoleMocksFactory.mockRoleEntity(2);
		
		user.addRole(role1);
		user.addRole(role2);
		
		Set<Role> resultRoles = user.getRoles();
		assertTrue(resultRoles.contains(expected1));
		assertTrue(resultRoles.contains(expected2));
		assertEquals(2, resultRoles.size());
	}
	
	@Test
	public void addRoleShouldNotAdRoleToListWhenRolesListAlreadyContainsTheRole() {
		User user = new User();
		Role role1 = RoleMocksFactory.mockRoleEntity(1);
		Role expected1 = RoleMocksFactory.mockRoleEntity(1);

		user.addRole(role1);
		user.addRole(role1);
		
		Set<Role> resultRoles = user.getRoles();
		assertTrue(resultRoles.contains(expected1));
		assertEquals(1, resultRoles.size());
	}
	
	@Test
	public void getAuthoritiesShouldReturnAEmptyCollectionWhenRolesListAreEmpty() {
		User user = new User();
		Collection<? extends GrantedAuthority> result = user.getAuthorities();
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void getAuthoritiesShouldReturnCorrectlyCollection() {
		User user = new User();
		Role role1 = RoleMocksFactory.mockRoleEntity(1);
		Role role2 = RoleMocksFactory.mockRoleEntity(2);
		GrantedAuthority expected1 = new SimpleGrantedAuthority(role1.getAuthority());
		GrantedAuthority expected2 = new SimpleGrantedAuthority(role2.getAuthority());
		
		user.addRole(role1);
		user.addRole(role2);
		
		Collection<? extends GrantedAuthority> result = user.getAuthorities();
		assertTrue(result.contains(expected1));
		assertTrue(result.contains(expected2));
		assertEquals(2, result.size());
	}
}
