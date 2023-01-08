package com.fronchak.locadora.mocks;

import com.fronchak.locadora.dtos.role.RoleInputDTO;
import com.fronchak.locadora.dtos.role.RoleOutputDTO;
import com.fronchak.locadora.entities.Role;

public class RoleMocksFactory {

	public static Role mockRoleEntity() {
		return mockRoleEntity(0);
	}
	
	public static Role mockRoleEntity(int i) {
		return new Role(mockId(i), mockAuthority(i));
	}
	
	private static Long mockId(int i) {
		return 20L + i;
	}
	
	private static String mockAuthority(int i) {
		return "ROLE_AUTHORITY_" + i;
	}
	
	public static RoleOutputDTO mockRoleOutputDTO() {
		return mockRoleOutputDTO(0);
	}
	
	public static RoleOutputDTO mockRoleOutputDTO(int i) {
		return new RoleOutputDTO(mockId(i), mockAuthority(i));
	}
	
	public static RoleInputDTO mockRoleInputDTO() {
		return mockRoleInputDTO(0);
	}
	
	public static RoleInputDTO mockRoleInputDTO(int i) {
		return new RoleInputDTO(mockId(i));
	}
}
