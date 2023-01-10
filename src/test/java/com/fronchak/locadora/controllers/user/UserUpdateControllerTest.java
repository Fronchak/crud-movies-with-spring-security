package com.fronchak.locadora.controllers.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.dtos.user.UserUpdateDTO;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.util.CustomizeControllerAsserts;

public class UserUpdateControllerTest extends AbstractUserControllerTest {

	private UserUpdateDTO updateDTO;
	private UserOutputDTO outputDTO;
	private String body;
	private ResultActions result;
	
	@BeforeEach
	public void setUp() {
		updateDTO = UserMocksFactory.mockUserUpdateDTO();
		outputDTO = UserMocksFactory.mockUserOutputDTO();
		doReturn(outputDTO).when(service).update(any(UserUpdateDTO.class), eq(EXISTING_ID));
		doThrow(ResourceNotFoundException.class).when(service).update(any(UserUpdateDTO.class), eq(NON_EXISTING_ID));
	}
	
	private void convertUpdateDTOToJson() throws Exception {
		body = mapper.writeValueAsString(updateDTO);
	}
	
	private void performPutMethodWithoutToken(Long id) throws Exception {
		result = mockMvc.perform(put("/users/{id}", id)
				.content(body)
				.contentType(MEDIA_TYPE)
				.accept(MEDIA_TYPE));
	}
	
	private void performPutMethodWithToken(Long id) throws Exception {
		result = mockMvc.perform(put("/users/{id}", id)
				.header("Authorization", "Bearer " + accessToken)
				.content(body)
				.contentType(MEDIA_TYPE)
				.accept(MEDIA_TYPE));
	}
	
	@Test
	public void updateShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		convertUpdateDTOToJson();
		
		performPutMethodWithoutToken(EXISTING_ID);
		
		CustomizeControllerAsserts.assertUnauthorized(result);
	}
	
	@Test
	public void updateShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		convertUpdateDTOToJson();
		getClientToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		CustomizeControllerAsserts.assertForbidden(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButEmailIsNull() throws Exception {
		updateDTO.setEmail(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullEmail(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButEmailIsNull() throws Exception {
		updateDTO.setEmail(null);
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullEmail(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButEmailIsNotWellFormated() throws Exception {
		updateDTO.setEmail("gabriel@gmail");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNotWellFormatedEmail(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButEmailIsNotWellFormated() throws Exception {
		updateDTO.setEmail("gabriel@gmail");
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNotWellFormatedEmail(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButPasswordIsNull() throws Exception {
		updateDTO.setPassword(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButPasswordIsNull() throws Exception {
		updateDTO.setPassword(null);
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButPasswordIsEmpty() throws Exception {
		updateDTO.setPassword("      ");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidEmptyPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButPasswordIsEmpty() throws Exception {
		updateDTO.setPassword("      ");
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidEmptyPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButPasswordHasLessThen6Letters() throws Exception {
		updateDTO.setPassword("12345");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidLessThan6LettersPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButPasswordHasLessThen6Letters() throws Exception {
		updateDTO.setPassword("12345");
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidLessThan6LettersPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButOldPasswordIsNull() throws Exception {
		updateDTO.setOldPassword(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullOldPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButOldPasswordIsNull() throws Exception {
		updateDTO.setOldPassword(null);
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullOldPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButOldPasswordIsEmpty() throws Exception {
		updateDTO.setOldPassword("       ");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidEmptyOldPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButOldPasswordIsEmpty() throws Exception {
		updateDTO.setOldPassword("       ");
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullOldPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButOldPasswordHasLessThen6Letters() throws Exception {
		updateDTO.setOldPassword("12345");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidLessThan6LettersOldPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButOldPasswordHasLessThen6Letters() throws Exception {
		updateDTO.setOldPassword("12345");
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidLessThan6LettersOldPassword(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButRolesIsNull() throws Exception {
		updateDTO.setRoles(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullRoles(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButRolesIsNull() throws Exception {
		updateDTO.setRoles(null);
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullRoles(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButRolesIsEmpty() throws Exception {
		updateDTO.getRoles().clear();
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidEmptyRoles(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButRolesIsEmpty() throws Exception {
		updateDTO.getRoles().clear();
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidEmptyRoles(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedButRoleIsNull() throws Exception {
		updateDTO.addRole(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullRole(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenAdminIsLoggedButRoleIsNull() throws Exception {
		updateDTO.addRole(null);
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertInvalidNullRole(result);
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenOperatorIsLoggedButIdDoesNotExist() throws Exception {
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(NON_EXISTING_ID);
		
		CustomizeControllerAsserts.assertNotFound(result);
		verify(service, times(1)).update(any(UserUpdateDTO.class), eq(NON_EXISTING_ID));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenAdminIsLoggedButIdDoesNotExist() throws Exception {
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(NON_EXISTING_ID);
		
		CustomizeControllerAsserts.assertNotFound(result);
		verify(service, times(1)).update(any(UserUpdateDTO.class), eq(NON_EXISTING_ID));
	}
	
	@Test
	public void updateShouldReturnSuccessWhenOperatorIsLoggedAndNoValidationsAreBroken() throws Exception {
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertSuccessAndOutputDTO(result);
		verify(service, times(1)).update(any(UserUpdateDTO.class), eq(EXISTING_ID));
	}
	
	@Test
	public void updateShouldReturnSuccessWhenAdminIsLoggedAndNoValidationsAreBroken() throws Exception {
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutMethodWithToken(EXISTING_ID);
		
		assertSuccessAndOutputDTO(result);
		verify(service, times(1)).update(any(UserUpdateDTO.class), eq(EXISTING_ID));
	}
}
