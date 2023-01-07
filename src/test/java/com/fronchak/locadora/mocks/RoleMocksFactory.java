package com.fronchak.locadora.mocks;

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
}
