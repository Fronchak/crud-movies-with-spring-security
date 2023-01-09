package com.fronchak.locadora.dtos.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fronchak.locadora.dtos.role.RoleInputDTO;

public class UserInputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "User's email must be specified")
	@Email(message = "Invalid email format, please try a valid email", 
	regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
	private String email;
	
	@NotNull(message = "User's password must be specified")
	@Size(min = 6, message = "User's password must have at least 6 letters")
	private String password;
	
	@NotNull(message = "User's roles must be specified")
	@NotEmpty(message = "User's roles cannot be empty, must have at least one role")
	private Set<@NotNull(message = "User's role must be specified") RoleInputDTO> roles = new HashSet<>();
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<RoleInputDTO> getRoles() {
		return roles;
	}

	public void addRole(RoleInputDTO role) {
		roles.add(role);
	}
	
	public void setRoles(Set<RoleInputDTO> roles) {
		this.roles = roles;
	}
}
