package com.fronchak.locadora.controllers.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.util.CustomizeControllerAsserts;

public class UserSaveControllerTest extends AbstractUserControllerTest {

	private UserInsertDTO insertDTO;
	private UserOutputDTO outputDTO;
	private String body;
	private ResultActions result;
	
	@BeforeEach
	public void setUp() {
		insertDTO = UserMocksFactory.mockUserInsertDTO();
		outputDTO = UserMocksFactory.mockUserOutputDTO();
		doReturn(outputDTO).when(service).save(any(UserInsertDTO.class));
	}
	
	private void convertInsertDTOToJson() throws Exception {
		body = mapper.writeValueAsString(insertDTO);
	}
	
	private void performPostWithoutToken() throws Exception {
		result = mockMvc.perform(post("/users")
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
	}
	
	private void performPostWithToken() throws Exception {
		result = mockMvc.perform(post("/users")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
	}
	
	@Test
	public void saveShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		convertInsertDTOToJson();
		
		performPostWithoutToken();
		
		CustomizeControllerAsserts.assertUnauthorized(result);
	}
	
	@Test
	public void saveShouldReturnFobiddenWhenClientIsLogged() throws Exception {
		convertInsertDTOToJson();
		getClientToken();
		
		performPostWithToken();
		
		CustomizeControllerAsserts.assertForbidden(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButEmailIsNull() throws Exception {
		insertDTO.setEmail(null);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNullEmail(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButEmailIsNull() throws Exception {
		insertDTO.setEmail(null);
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidNullEmail(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButEmailIsNotWellFormated() throws Exception {
		insertDTO.setEmail("gabriel@mail");
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNotWellFormatedEmail(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButEmailIsNotWellFormated() throws Exception {
		insertDTO.setEmail("gabriel@mail");
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidNotWellFormatedEmail(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButPasswordIsNull() throws Exception {
		insertDTO.setPassword(null);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNullPassword(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButPasswordIsNull() throws Exception {
		insertDTO.setPassword(null);
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidNullPassword(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButPasswordHasLessThan6Letters() throws Exception {
		insertDTO.setPassword("12345");
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidLessThan6LettersPassword(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButPasswordHasLessThan6Letters() throws Exception {
		insertDTO.setPassword("12345");
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidLessThan6LettersPassword(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButRolesIsNull() throws Exception {
		insertDTO.setRoles(null);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNullRoles(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButRolesIsNull() throws Exception {
		insertDTO.setRoles(null);
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidNullRoles(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButRolesIsEmpty() throws Exception {
		insertDTO.getRoles().clear();
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidEmptyRoles(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButRolesIsEmpty() throws Exception {
		insertDTO.getRoles().clear();
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidEmptyRoles(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedButRoleIsNull() throws Exception {
		insertDTO.addRole(null);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNullRole(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenAdminIsLoggedButRoleIsNull() throws Exception {
		insertDTO.addRole(null);
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertInvalidNullRole(result);
	}
	
	@Test
	public void saveShouldReturnCreatedWhenOperatorIsLoggedAndNoValidationAreBroken() throws Exception {
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertCreatedAndOutputDTO(result);
		verify(service, times(1)).save(any(UserInsertDTO.class));
	}
	
	@Test
	public void saveShouldReturnCreatedWhenAdminIsLoggedAndNoValidationAreBroken() throws Exception {
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertCreatedAndOutputDTO(result);
		verify(service, times(1)).save(any(UserInsertDTO.class));
	}
}
