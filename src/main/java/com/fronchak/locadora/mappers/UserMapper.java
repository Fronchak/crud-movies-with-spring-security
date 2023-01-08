package com.fronchak.locadora.mappers;

import org.springframework.stereotype.Service;

import com.fronchak.locadora.dtos.role.RoleOutputDTO;
import com.fronchak.locadora.dtos.user.UserInputDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.entities.Role;
import com.fronchak.locadora.entities.User;

@Service
public class UserMapper {

	public UserOutputDTO convertEntityToOutputDTO(User entity) {
		UserOutputDTO dto = new UserOutputDTO();
		dto.setId(entity.getId());
		dto.setEmail(entity.getEmail());
		entity.getRoles().forEach(role -> dto.addRole(convertRoleEntityToRoleOutputDTO(role)));
		return dto;
	}
	
	private RoleOutputDTO convertRoleEntityToRoleOutputDTO(Role entity) {
		return new RoleOutputDTO(entity);
	}
	
	public void copyDTOToEntity(UserInputDTO dto, User entity) {
		entity.setEmail(dto.getEmail());
	}
}
