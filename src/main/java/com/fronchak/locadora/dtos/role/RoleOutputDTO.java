package com.fronchak.locadora.dtos.role;

import java.io.Serializable;
import java.util.Objects;

import com.fronchak.locadora.entities.Role;

public class RoleOutputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String authority;
	
	public RoleOutputDTO() {}
	
	public RoleOutputDTO(Long id, String authority) {
		this.id = id;
		this.authority = authority;
	}
	
	public RoleOutputDTO(Role entity) {
		this(entity.getId(), entity.getAuthority());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleOutputDTO other = (RoleOutputDTO) obj;
		return Objects.equals(id, other.id);
	}
}
