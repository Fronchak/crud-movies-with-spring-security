package com.fronchak.locadora.controllers.movie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.mocks.MovieMocksFactory;

public class MovieSaveControllerTest extends AbstractMovieControllerTest {

	private MovieInsertDTO insertDTO;
	private MovieOutputDTO outputDTO;
	private String body;

	private ResultActions result;
	
	@BeforeEach
	public void setUp() {
		insertDTO = MovieMocksFactory.mockMovieInsertDTO();
		outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.save(any(MovieInsertDTO.class))).thenReturn(outputDTO);
	}
	
	private void convertInsertDTOToJson() throws Exception {
		body = mapper.writeValueAsString(insertDTO);
	}
	
	private void performPostWithoutToken() throws Exception {
		result = mockMvc.perform(post("/movies")
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
	}
	
	private void performPostWithToken() throws Exception {
		result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
	}
	
	@Test
	public void saveShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {		
		convertInsertDTOToJson();
		
		performPostWithoutToken();
		
		assertUnauthorized(result);
	}
	
	@Test
	public void saveShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		convertInsertDTOToJson();
		getClientToken();
		
		performPostWithToken();
		
		assertForbidden(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndTitleIsBlank() throws Exception {
		insertDTO.setTitle("   ");
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidBlankTitle(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndSynopsisIsBlank() throws Exception {
		insertDTO.setSynopsis("  ");
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidBlankSynopsis(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationIsNull() throws Exception {
		insertDTO.setDurationInMinutes(null);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNullDurationInMinutes(result);
	}
	
	@Test
	public void saveShouldReturnUnprecessableEntityWhenOperatorIsLoggedAndDurationInZero() throws Exception {
		insertDTO.setDurationInMinutes(0);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidZeroDurationInMinutes(result);
	}
	
	@Test
	public void saveShouldReturnUnprecessableEntityWhenOperatorIsLoggedAndDurationInBiggerThan240Minutes() throws Exception {
		insertDTO.setDurationInMinutes(241);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidMoreThan240DurationInMinutes(result);
	}
	
	@Test
	public void saveShouldReturnUnprecessableEntityWhenOperatorIsLoggedAndNoteIsNull() throws Exception {
		insertDTO.setNote(null);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNullNote(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsNegative() throws Exception {
		insertDTO.setNote(-0.1);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidNegativeNote(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsBiggerThanFive() throws Exception {
		insertDTO.setNote(5.1);
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertInvalidMoreThanFiveNote(result);
	}
	
	@Test
	public void saveShouldReturnCreatedWhenOperatorIsLoggedAndNoValidationsAreBroken() throws Exception {
		convertInsertDTOToJson();
		getOperatorToken();
		
		performPostWithToken();
		
		assertCreatedAndMovieOutputDTO(result);
	}
	
	@Test
	public void saveShouldReturnCreatedWhenAdminIsLoggedAndNoValidationsAreBroken() throws Exception {
		convertInsertDTOToJson();
		getAdminToken();
		
		performPostWithToken();
		
		assertCreatedAndMovieOutputDTO(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndThereIsAnotherMovieWithThaSameTitleAlreadySaved() throws Exception {
		insertDTO.setTitle(EXIST_TITLE);
		convertInsertDTOToJson();
		Movie entity = MovieMocksFactory.mockMovieEntity();		
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		getOperatorToken();

		performPostWithToken();
		
		assertInvalidDuplicateTitle(result);
	}
}
