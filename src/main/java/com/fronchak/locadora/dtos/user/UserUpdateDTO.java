package com.fronchak.locadora.dtos.user;

public class UserUpdateDTO extends UserInputDTO {

	private static final long serialVersionUID = 1L;
	
	private String oldPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
