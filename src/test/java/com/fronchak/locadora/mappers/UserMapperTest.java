package com.fronchak.locadora.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.dtos.user.UserUpdateDTO;
import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class UserMapperTest {

	private UserMapper mapper;
	
	@BeforeEach
	public void setUp() {
		mapper = new UserMapper();
	}
	
	@Test
	public void convertEntityToOutputDTOShouldConvertCorrectly() {
		User entity = UserMocksFactory.mockUserEntity();
		UserOutputDTO result = mapper.convertEntityToOutputDTO(entity);
		CustomizeAsserts.assertUserOutputDTO(result);
	}
	
	@Test
	public void copyDTOToEntityUsingInsertDTOShouldCopyValuesCorrectly() {
		UserInsertDTO dto = UserMocksFactory.mockUserInsertDTO();
		User entity = new User();
		mapper.copyDTOToEntity(dto, entity);
		assertNull(entity.getId());
		assertEquals("mock_email_0@gmail.com.br", entity.getEmail());
	}
	
	@Test
	public void copyDTOToEntityUsingUpdateDTOShouldCopyValuesCorrectly() {
		UserUpdateDTO dto = UserMocksFactory.mockUserUpdateDTO();
		User entity = new User();
		entity.setId(3L);
		mapper.copyDTOToEntity(dto, entity);
		assertEquals(3L, entity.getId());
		assertEquals("mock_email_0@gmail.com.br", entity.getEmail());
	}
}
