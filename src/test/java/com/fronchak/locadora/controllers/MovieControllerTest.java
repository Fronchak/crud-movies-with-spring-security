package com.fronchak.locadora.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mocks.MovieMocksFactory;
import com.fronchak.locadora.services.MovieService;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {
	
	private static final Long VALID_ID = 1L;
	private static final Long INVALID_ID = 10000L;
	private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MovieService service;
	
	@Test
	public void findByIdShouldReturnSuccessWhenIdExists() throws Exception {
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.findById(VALID_ID)).thenReturn(outputDTO);
		
		ResultActions result = mockMvc.perform(get("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		when(service.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		ResultActions result = mockMvc.perform(get("/movies/{id}", INVALID_ID)
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Entity not found"));
	}
	
	@Test
	public void findAllPagedShouldReturnDTOPage() throws Exception {
		Page<MovieOutputAllDTO> page = MovieMocksFactory.mockMovieOutputAllDTOPage();
		when(service.findAllPaged(any(Pageable.class))).thenReturn(page);
		
		ResultActions result = mockMvc.perform(get("/movies?page=0&size=10")
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").exists());
		result.andExpect(jsonPath("$.content[1].id").exists());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/movies/{id}", VALID_ID)
				.accept(MEDIA_TYPE));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		doThrow(ResourceNotFoundException.class).when(service).delete(INVALID_ID);
		
		ResultActions result = mockMvc.perform(delete("/movies/{id}", INVALID_ID)
				.accept(MEDIA_TYPE));
	
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Entity not found"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableEntityWhenTitleIsBlank() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setTitle("   ");
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprocessableEntityWhenSynopsisIsBlank() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setSynopsis("  ");
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprocessableEntityWhenDurationIsNull() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setDurationInMinutes(null);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprecessableEntityWhenDurationInZero() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setDurationInMinutes(0);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprecessableEntityWhenDurationInBiggerThan240Minutes() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setDurationInMinutes(241);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprecessableEntityWhenNoteIsNull() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setNote(null);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprecessableEntityWhenNoteIsNegative() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setNote(-0.1);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnUnprecessableEntityWhenNoteIsBiggerThanFive() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		dto.setNote(5.1);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
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
	public void saveShouldReturnCreatedWhenNoValidationsAreBroken() throws Exception {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		when(service.save(any(MovieInsertDTO.class))).thenReturn(outputDTO);
		
		String body = mapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(post("/movies")
				.accept(MEDIA_TYPE)
				.content(body)
				.contentType(MEDIA_TYPE));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
	}
}