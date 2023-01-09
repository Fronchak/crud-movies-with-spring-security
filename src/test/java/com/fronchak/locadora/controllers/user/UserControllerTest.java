package com.fronchak.locadora.controllers.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.exceptions.DatabaseException;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mocks.UserMocksFactory;
import com.fronchak.locadora.util.CustomizeControllerAsserts;

public class UserControllerTest extends AbstractUserControllerTest {

	@Test
	public void findByIdShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(get("/users/{id}", NON_EXISTING_ID)
				.accept(MEDIA_TYPE));
		CustomizeControllerAsserts.assertUnauthorized(result);
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		getClientToken();
		ResultActions result = mockMvc.perform(get("/users/{id}", EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		CustomizeControllerAsserts.assertForbidden(result);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenOperatorIsLoggedButIsDoesNotExist() throws Exception {
		when(service.findById(NON_EXISTING_ID)).thenThrow(ResourceNotFoundException.class);
		getOperatorToken();
		
		ResultActions result = mockMvc.perform(get("/users/{id}", NON_EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertNotFound(result);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenAdminIsLoggedButIsDoesNotExist() throws Exception {
		when(service.findById(NON_EXISTING_ID)).thenThrow(ResourceNotFoundException.class);
		getAdminToken();
		
		ResultActions result = mockMvc.perform(get("/users/{id}", NON_EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertNotFound(result);
	}
	
	@Test
	public void findByIdShouldReturnOutputDTOWhenOperatorIsLoggedAndIdExists() throws Exception {
		UserOutputDTO outputDTO = UserMocksFactory.mockUserOutputDTO();
		doReturn(outputDTO).when(service).findById(EXISTING_ID);
		getOperatorToken();
		
		ResultActions result = mockMvc.perform(get("/users/{id}", EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertSuccessAndOutputDTO(result);
	}
	
	@Test
	public void findByIdShouldReturnOutputDTOWhenAdminIsLoggedAndIdExists() throws Exception {
		UserOutputDTO outputDTO = UserMocksFactory.mockUserOutputDTO();
		doReturn(outputDTO).when(service).findById(EXISTING_ID);
		getAdminToken();
		
		ResultActions result = mockMvc.perform(get("/users/{id}", EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertSuccessAndOutputDTO(result);
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		doNothing().when(service).delete(any());
		
		ResultActions result = mockMvc.perform(delete("/users/{id}", EXISTING_ID)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertUnauthorized(result);
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		doNothing().when(service).delete(any());
		getClientToken();
		
		ResultActions result = mockMvc.perform(delete("/users/{id}", EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertForbidden(result);
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenOperatorIsLogged() throws Exception {
		doNothing().when(service).delete(any());
		getOperatorToken();
		
		ResultActions result = mockMvc.perform(delete("/users/{id}", EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertForbidden(result);
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenAdminIsLogged() throws Exception {
		doNothing().when(service).delete(any());
		getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/users/{id}", EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertNoContent(result);
		verify(service, times(1)).delete(EXISTING_ID);
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenAdminIsLoggedButIdDoesNotExist() throws Exception {
		doThrow(ResourceNotFoundException.class).when(service).delete(NON_EXISTING_ID);
		getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/users/{id}", NON_EXISTING_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertNotFound(result);
		verify(service, times(1)).delete(NON_EXISTING_ID);
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenAdminIsLoggedButUserCannotBeDeleted() throws Exception {
		doThrow(DatabaseException.class).when(service).delete(DEPENDENT_ID);
		getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/users/{id}", DEPENDENT_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		CustomizeControllerAsserts.assertBadRequestAndDatabaseException(result);
		verify(service, times(1)).delete(DEPENDENT_ID);
	}
}
