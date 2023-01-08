package com.fronchak.locadora.dtos.role;

import java.io.Serializable;
import java.util.Objects;

public class RoleInputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	public RoleInputDTO() {}
	
	public RoleInputDTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		RoleInputDTO other = (RoleInputDTO) obj;
		return Objects.equals(id, other.id);
	}
}
