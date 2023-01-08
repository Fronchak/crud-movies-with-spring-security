package com.fronchak.locadora.mocks;

import com.fronchak.locadora.dtos.user.UserInputDTO;
import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.dtos.user.UserUpdateDTO;
import com.fronchak.locadora.entities.User;

public class UserMocksFactory {

	public static User mockUserEntity() {
		return mockUserEntity(0);
	}
	
	public static User mockUserEntity(int i) {
		User mock = new User();
		mock.setId(mockId(i));
		mock.setEmail(mockEmail(i));
		mock.setPassword(mockPassword(i));
		mock.addRole(RoleMocksFactory.mockRoleEntity(i));
		mock.addRole(RoleMocksFactory.mockRoleEntity(i + 1));
		return mock;
	}
	
	private static Long mockId(int i) {
		return 30L + i;
	}
	
	private static String mockEmail(int i ) {
		return "mock_email_" + i + "@gmail.com.br";
	}
	
	private static String mockPassword(int i) {
		return "Encode123456-" + i;
	}
	
	public static UserInsertDTO mockUserInsertDTO() {
		return mockUserInsertDTO(0);
	}
	
	public static UserInsertDTO mockUserInsertDTO(int i) {
		return (UserInsertDTO) mockUserInputDTO(new UserInsertDTO(), i);
	}
	
	private static UserInputDTO mockUserInputDTO(UserInputDTO mock, int i) {
		mock.setEmail(mockEmail(i));
		mock.setPassword(mockRawPassword(i));
		mock.addRole(RoleMocksFactory.mockRoleInputDTO(i));
		mock.addRole(RoleMocksFactory.mockRoleInputDTO(i + 1));
		return mock;
	}
	
	private static String mockRawPassword(int i) {
		return "Raw123456-" + i;
	}
	
	public static UserUpdateDTO mockUserUpdateDTO() {
		return mockUserUpdateDTO(0);
	}
	
	public static UserUpdateDTO mockUserUpdateDTO(int i) {
		return (UserUpdateDTO) mockUserInputDTO(new UserUpdateDTO(), i);
	}
	
	public static UserOutputDTO mockUserOutputDTO() {
		return mockUserOutputDTO(0);
	}
	
	public static UserOutputDTO mockUserOutputDTO(int i) {
		UserOutputDTO mock = new UserOutputDTO();
		mock.setId(mockId(i));
		mock.setEmail(mockEmail(i));
		mock.addRole(RoleMocksFactory.mockRoleOutputDTO(i));
		mock.addRole(RoleMocksFactory.mockRoleOutputDTO(i + 1));
		return mock;
	}
}
