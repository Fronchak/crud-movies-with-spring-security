package com.fronchak.locadora.controllers.movie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mocks.MovieMocksFactory;

public class MovieControllerTest extends AbstractMovieControllerTest {

	@Test
	public void findByIdShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(get("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE));
		assertUnauthorized(result);
	}
	
	@Test
	public void findByIdShouldReturnSuccessWhenClientIsLoggedAndIdExists() throws Exception {
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.findById(VALID_ID)).thenReturn(outputDTO);
		getClientToken();
		
		ResultActions result = mockMvc.perform(get("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		

		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenClientIsLoggedButIdDoesNotExists() throws Exception {
		when(service.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		getClientToken();
		
		ResultActions result = mockMvc.perform(get("/movies/{id}", INVALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertNotFound(result);
	}

	@Test
	public void findAllPagedShouldReturnUnauthorizedWhenClientIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(get("/movies?page=0&size=10")
				.accept(MEDIA_TYPE));
		assertUnauthorized(result);
	}
	
	@Test
	public void findAllPagedShouldReturnDTOPageWhenClienIsLogged() throws Exception {
		Page<MovieOutputAllDTO> page = MovieMocksFactory.mockMovieOutputAllDTOPage();
		when(service.findAllPaged(any(Pageable.class))).thenReturn(page);
		getClientToken();
		
		ResultActions result = mockMvc.perform(get("/movies?page=0&size=10")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertSuccessAndMovieOutputDTOPage(result);
	}
	
	@Test
	public void deleteUnauthorizedWhenUserIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE));
		assertUnauthorized(result);
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenUserIsLogged() throws Exception {
		getClientToken();
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertForbidden(result);
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenOperatrIsLogged() throws Exception {
		getOperatorToken();
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertForbidden(result);
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenAdminIsLoggedAndIdExists() throws Exception {
		getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenAdminIsLoggedButIdDoesNotExist() throws Exception {
		doThrow(ResourceNotFoundException.class).when(service).delete(INVALID_ID);
		getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", INVALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
	
		assertNotFound(result);
	}
}
