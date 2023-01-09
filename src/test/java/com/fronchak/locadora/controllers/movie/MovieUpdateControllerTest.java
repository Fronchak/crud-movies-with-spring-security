package com.fronchak.locadora.controllers.movie;

import static com.fronchak.locadora.util.CustomizeControllerAsserts.assertForbidden;
import static com.fronchak.locadora.util.CustomizeControllerAsserts.assertNotFound;
import static com.fronchak.locadora.util.CustomizeControllerAsserts.assertUnauthorized;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.dtos.movie.MovieUpdateDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mocks.MovieMocksFactory;

public class MovieUpdateControllerTest extends AbstractMovieControllerTest {

	private MovieUpdateDTO updateDTO;
	private MovieOutputDTO outputDTO;
	private String body;
	private ResultActions result;
	
	@BeforeEach	
	public void setUp() {
		updateDTO = MovieMocksFactory.mockMovieUpdateDTO();	
		outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.update(any(MovieUpdateDTO.class), eq(VALID_ID))).thenReturn(outputDTO);
		when(service.update(any(MovieUpdateDTO.class), eq(INVALID_ID))).thenThrow(ResourceNotFoundException.class);
	}
	
	private void convertUpdateDTOToJson() throws Exception {
		body = mapper.writeValueAsString(updateDTO);
	}
	
	private void performPutWithoutToken(Long id) throws Exception {
		result = mockMvc.perform(put("/movies/{id}", id)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
	}
	
	private void performPutWithToken(Long id) throws Exception {
		result = mockMvc.perform(put("/movies/{id}", id)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
	}
	
	@Test
	public void updateShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		convertUpdateDTOToJson();

		performPutWithoutToken(VALID_ID);
		
		assertUnauthorized(result);
	}
	
	@Test
	public void updateShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		convertUpdateDTOToJson();
		getClientToken();
		
		performPutWithToken(VALID_ID);
		
		assertForbidden(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndTitleIsBlank() throws Exception {
		updateDTO.setTitle("  ");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidBlankTitle(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndSynopsisIsBlank() throws Exception {
		updateDTO.setSynopsis("  ");
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidBlankSynopsis(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationInMinutesIsNull() throws Exception {
		updateDTO.setDurationInMinutes(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidNullDurationInMinutes(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationInMinutesIsZeroOrLower() throws Exception {
		updateDTO.setDurationInMinutes(0);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidZeroDurationInMinutes(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationInMinutesIsBiggerThan240Minutes() throws Exception {
		updateDTO.setDurationInMinutes(241);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidMoreThan240DurationInMinutes(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsNull() throws Exception {
		updateDTO.setNote(null);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidNullNote(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsNegative() throws Exception {
		updateDTO.setNote(-0.1);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidNegativeNote(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsBiggerThanFive() throws Exception {
		updateDTO.setNote(5.1);
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidMoreThanFiveNote(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenOperatorIsLoggedAndNoValidationsAreBrokenAndIdExists() throws Exception {
		convertUpdateDTOToJson();
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenAdminIsLoggedAndNoValidationsAreBrokenAndIdExists() throws Exception {
		convertUpdateDTOToJson();
		getAdminToken();
		
		performPutWithToken(VALID_ID);
		
		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenOperatorIsLoggedAndNoValidationsAreBrokenButIdDoesNotExist() throws Exception {
		convertUpdateDTOToJson();
		getOperatorToken();

		performPutWithToken(INVALID_ID);
		
		assertNotFound(result);
	}
	
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndThereIsAnotherMovieWithTheSameNameAlreadySaved() throws Exception {
		updateDTO.setTitle(EXIST_TITLE);
		convertUpdateDTOToJson();
		Movie entity = MovieMocksFactory.mockMovieEntity();
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		getOperatorToken();
		
		performPutWithToken(VALID_ID);
		
		assertInvalidDuplicateTitle(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenOperatorIsLoggedAndExistAnotherMovieWithTheSameTitleButIsTheEntityBeenUpdated() throws Exception {
		updateDTO.setTitle(EXIST_TITLE);
		convertUpdateDTOToJson();
		Movie entity = MovieMocksFactory.mockMovieEntity();
		entity.setId(VALID_ID);
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		getOperatorToken();

		performPutWithToken(VALID_ID);
		
		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenAdminIsLoggedAndExistAnotherMovieWithTheSameTitleButIsTheEntityBeenUpdated() throws Exception {
		updateDTO.setTitle(EXIST_TITLE);
		convertUpdateDTOToJson();
		Movie entity = MovieMocksFactory.mockMovieEntity();
		entity.setId(VALID_ID);
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		getAdminToken();

		performPutWithToken(VALID_ID);
		
		assertSuccessAndMovieOutputDTO(result);
	}
}
