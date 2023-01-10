package com.fronchak.locadora.dtos.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserUpdateDTO extends UserInputDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "User's old password cannot be empty")
	@Size(min = 6, message = "User's old password must have at least 6 letters")
	private String oldPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
