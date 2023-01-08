package com.fronchak.locadora.dtos.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fronchak.locadora.dtos.role.RoleInputDTO;

public class UserInputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private Set<RoleInputDTO> roles = new HashSet<>();
	
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
}
