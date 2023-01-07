package com.fronchak.locadora.mocks;

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
		return mock;
	}
	
	private static Long mockId(int i) {
		return 30L + i;
	}
	
	private static String mockEmail(int i ) {
		return "mock_email_" + i + "@gmail.com.br";
	}
	
	private static String mockPassword(int i) {
		return "123456-" + i;
	}
}
