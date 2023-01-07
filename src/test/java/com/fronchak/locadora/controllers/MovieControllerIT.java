package com.fronchak.locadora.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.dtos.movie.MovieUpdateDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mocks.MovieMocksFactory;
import com.fronchak.locadora.repositories.MovieRepository;
import com.fronchak.locadora.services.MovieService;
import com.fronchak.locadora.util.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIT {
	
	private static final Long VALID_ID = 1L;
	private static final Long INVALID_ID = 10000L;
	private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;
	
	private static final String EXIST_TITLE = "Harry Potter and the Prisoner of Azkaban";
	
	private static final String CLIENT_USERNAME = "gmack@gmail.com";
	private static final String CLIENT_PASSWORD = "123456";
	private static final String OPERATOR_USERNAME = "gabriel@gmail.com";
	private static final String OPERATOR_PASSWORD = "123456";
	private static final String ADMIN_USERNAME = "fronchak@gmail.com";
	private static final String ADMIN_PASSWORD = "123456";
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@MockBean
	private MovieService service;
	
	@MockBean
	private MovieRepository repository;
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(get("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE));
		assertUnauthorized(result);
	}
	
	private void assertUnauthorized(ResultActions result) throws Exception {
		result.andExpect(status().isUnauthorized());
		result.andExpect(jsonPath("$.error").value("unauthorized"));
	}
	
	@Test
	public void findByIdShouldReturnSuccessWhenClientIsLoggedAndIdExists() throws Exception {
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.findById(VALID_ID)).thenReturn(outputDTO);
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
		
		ResultActions result = mockMvc.perform(get("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		

		assertSuccessAndMovieOutputDTO(result);
	}
	
	private void assertSuccessAndMovieOutputDTO(ResultActions result) throws Exception {
		assertSuccess(result);
		assertMovieOutputDTO(result);
	}
	
	private void assertSuccess(ResultActions result) throws Exception {
		result.andExpect(status().isOk());
	}
	
	private void assertMovieOutputDTO(ResultActions result) throws Exception {
		result.andExpect(jsonPath("$.id").value(10L));
		result.andExpect(jsonPath("$.title").value("Mock movie title 0"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenClientIsLoggedButIdDoesNotExists() throws Exception {
		when(service.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
		
		ResultActions result = mockMvc.perform(get("/movies/{id}", INVALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertNotFound(result);
	}
	
	private void assertNotFound(ResultActions result) throws Exception {
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Entity not found"));
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
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
		
		ResultActions result = mockMvc.perform(get("/movies?page=0&size=10")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(10L));
		result.andExpect(jsonPath("$.content[1].id").value(11L));
	}
	
	@Test
	public void deleteUnauthorizedWhenUserIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE));
		assertUnauthorized(result);
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenUserIsLogged() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertForbidden(result);
	}
	
	private void assertForbidden(ResultActions result) throws Exception {
		result.andExpect(status().isForbidden());
		result.andExpect(jsonPath("$.error").value("access_denied"));
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenOperatrIsLogged() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		assertForbidden(result);
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenAdminIsLoggedAndIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenAdminIsLoggedButIdDoesNotExist() throws Exception {
		doThrow(ResourceNotFoundException.class).when(service).delete(INVALID_ID);
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
		ResultActions result = mockMvc.perform(delete("/movies/{id}", INVALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE));
	
		assertNotFound(result);
	}
	
	@Test
	public void saveShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertUnauthorized(result);
	}
	
	@Test
	public void saveShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertForbidden(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndTitleIsBlank() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setTitle("   ");
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidBlankTitle(result);
	}
	
	private void assertUnprocessableEntity(ResultActions result) throws Exception {
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.error").value("Validation error"));
	}
	
	private void assertInvalidBlankTitle(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("title"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's title cannot be empty"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndSynopsisIsBlank() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setSynopsis("  ");
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidBlankSynopsis(result);
	}
	
	private void assertInvalidBlankSynopsis(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("synopsis"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's synopsis cannot be empty"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationIsNull() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setDurationInMinutes(null);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidNullDurationInMinutes(result);
	}
	
	private void assertInvalidNullDurationInMinutes(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("durationInMinutes"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's duration must be specified"));
	}
	
	@Test
	public void saveShouldReturnUnprecessableEntityWhenOperatorIsLoggedAndDurationInZero() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setDurationInMinutes(0);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidZeroDurationInMinutes(result);
	}
	
	private void assertInvalidZeroDurationInMinutes(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("durationInMinutes"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's duration must have at least one minute"));
	}
	
	@Test
	public void saveShouldReturnUnprecessableEntityWhenOperatorIsLoggedAndDurationInBiggerThan240Minutes() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setDurationInMinutes(241);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidMoreThan240DurationInMinutes(result);
	}
	
	private void assertInvalidMoreThan240DurationInMinutes(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("durationInMinutes"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's duration cannot be bigger than 240 minutes"));
	}
	
	@Test
	public void saveShouldReturnUnprecessableEntityWhenOperatorIsLoggedAndNoteIsNull() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setNote(null);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidNullNote(result);
	}
	
	private void assertInvalidNullNote(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("note"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's note must be specified"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsNegative() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setNote(-0.1);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidNegativeNote(result);
	}
	
	private void assertInvalidNegativeNote(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("note"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's note cannot be negative"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsBiggerThanFive() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setNote(5.1);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidMoreThanFiveNote(result);
	}
	
	private void assertInvalidMoreThanFiveNote(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("note"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's note cannot be bigger than five"));
	}
	
	@Test
	public void saveShouldReturnCreatedWhenOperatorIsLoggedAndNoValidationsAreBroken() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.save(any(MovieInsertDTO.class))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertCreatedAndMovieOutputDTO(result);
	}
	
	private void assertCreatedAndMovieOutputDTO(ResultActions result) throws Exception {
		assertCreated(result);
		assertMovieOutputDTO(result);
	}
	
	private void assertCreated(ResultActions result) throws Exception {
		result.andExpect(status().isCreated());
	}
	
	@Test
	public void saveShouldReturnCreatedWhenAdminIsLoggedAndNoValidationsAreBroken() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.save(any(MovieInsertDTO.class))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertCreatedAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertUnauthorized(result);
	}
	
	@Test
	public void updateShouldReturnForbiddenWhenClientIsLogged() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertForbidden(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndTitleIsBlank() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setTitle("  ");
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidBlankTitle(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndSynopsisIsBlank() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setSynopsis("  ");
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidBlankSynopsis(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationInMinutesIsNull() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setDurationInMinutes(null);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidNullDurationInMinutes(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationInMinutesIsZeroOrLower() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setDurationInMinutes(0);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidZeroDurationInMinutes(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndDurationInMinutesIsBiggerThan240Minutes() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setDurationInMinutes(241);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidMoreThan240DurationInMinutes(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsNull() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setNote(null);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidNullNote(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsNegative() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setNote(-0.1);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidNegativeNote(result);
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndNoteIsBiggerThanFive() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setNote(5.1);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidMoreThanFiveNote(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenOperatorIsLoggedAndNoValidationsAreBrokenAndIdExists() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.update(any(MovieUpdateDTO.class), eq(VALID_ID))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenAdminIsLoggedAndNoValidationsAreBrokenAndIdExists() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.update(any(MovieUpdateDTO.class), eq(VALID_ID))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenOperatorIsLoggedAndNoValidationsAreBrokenButIdDoesNotExist() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		when(service.update(any(MovieUpdateDTO.class), eq(INVALID_ID))).thenThrow(ResourceNotFoundException.class);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", INVALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertNotFound(result);
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndThereIsAnotherMovieWithThaSameTitleAlreadySaved() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		Movie entity = MovieMocksFactory.mockMovieEntity();	
		dto.setTitle(EXIST_TITLE);
			
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
				
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidDuplicateTitle(result);
	}

	private void assertInvalidDuplicateTitle(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("title"));
		result.andExpect(jsonPath("$.errors[0].message").value("There is another movie with the same title already saved"));
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenOperatorIsLoggedAndThereIsAnotherMovieWithTheSameNameAlreadySaved() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setTitle(EXIST_TITLE);
		Movie entity = MovieMocksFactory.mockMovieEntity();
		
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertInvalidDuplicateTitle(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenOperatorIsLoggedAndExistAnotherMovieWithTheSameTitleButIsTheEntityBeenUpdated() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setTitle(EXIST_TITLE);
		Movie entity = MovieMocksFactory.mockMovieEntity();
		entity.setId(VALID_ID);
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		when(service.update(any(MovieUpdateDTO.class), eq(VALID_ID))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertSuccessAndMovieOutputDTO(result);
	}
	
	@Test
	public void updateShouldReturnSuccessWhenAdminIsLoggedAndExistAnotherMovieWithTheSameTitleButIsTheEntityBeenUpdated() throws Exception {
		MovieUpdateDTO dto = MovieMocksFactory.mockMovieUpdateDTO();
		dto.setTitle(EXIST_TITLE);
		Movie entity = MovieMocksFactory.mockMovieEntity();
		entity.setId(VALID_ID);
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		
		when(repository.findByTitle(EXIST_TITLE)).thenReturn(entity);
		when(service.update(any(MovieUpdateDTO.class), eq(VALID_ID))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
		
		ResultActions result = mockMvc.perform(put("/movies/{id}", VALID_ID)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		assertSuccessAndMovieOutputDTO(result);
	}
}
