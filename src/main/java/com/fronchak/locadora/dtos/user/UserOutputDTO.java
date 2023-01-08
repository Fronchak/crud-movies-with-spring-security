package com.fronchak.locadora.dtos.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fronchak.locadora.dtos.role.RoleOutputDTO;

public class UserOutputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private Set<RoleOutputDTO> roles = new HashSet<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<RoleOutputDTO> getRoles() {
		return roles;
	}
	
	public void addRole(RoleOutputDTO role) {
		roles.add(role);
	}
}
