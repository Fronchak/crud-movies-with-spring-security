package com.fronchak.locadora.controllers.movie;

import static com.fronchak.locadora.util.CustomizeControllerAsserts.assertCreated;
import static com.fronchak.locadora.util.CustomizeControllerAsserts.assertSuccess;
import static com.fronchak.locadora.util.CustomizeControllerAsserts.assertUnprocessableEntity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.locadora.repositories.MovieRepository;
import com.fronchak.locadora.services.MovieService;
import com.fronchak.locadora.util.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractMovieControllerTest {
	
	protected static final Long VALID_ID = 1L;
	protected static final Long INVALID_ID = 10000L;
	protected static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;
	
	protected static final String EXIST_TITLE = "Harry Potter and the Prisoner of Azkaban";
	
	private static final String CLIENT_USERNAME = "gmack@gmail.com";
	private static final String CLIENT_PASSWORD = "123456";
	private static final String OPERATOR_USERNAME = "gabriel@gmail.com";
	private static final String OPERATOR_PASSWORD = "123456";
	private static final String ADMIN_USERNAME = "fronchak@gmail.com";
	private static final String ADMIN_PASSWORD = "123456";
	
	protected String accessToken;
	
	@Autowired
	protected ObjectMapper mapper;
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected TokenUtil tokenUtil;
	
	@MockBean
	protected MovieService service;
	
	@MockBean
	protected MovieRepository repository;
	
	protected void getClientToken() throws Exception {
		accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
	}
	
	protected void getOperatorToken() throws Exception {
		accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
	}
	
	protected void getAdminToken() throws Exception {
		accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
	}

	protected void assertSuccessAndMovieOutputDTO(ResultActions result) throws Exception {
		assertSuccess(result);
		assertMovieOutputDTO(result);
	}

	protected void assertMovieOutputDTO(ResultActions result) throws Exception {
		result.andExpect(jsonPath("$.id").value(10L));
		result.andExpect(jsonPath("$.title").value("Mock movie title 0"));
	}
	
	protected void assertSuccessAndMovieOutputDTOPage(ResultActions result) throws Exception {
		assertSuccess(result);
		result.andExpect(jsonPath("$.content[0].id").value(10L));
		result.andExpect(jsonPath("$.content[0].title").value("Mock movie title 0"));
		result.andExpect(jsonPath("$.content[1].id").value(11L));
		result.andExpect(jsonPath("$.content[1].title").value("Mock movie title 1"));
	}

	protected void assertInvalidBlankTitle(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("title"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's title cannot be empty"));
	}

	protected void assertInvalidBlankSynopsis(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("synopsis"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's synopsis cannot be empty"));
	}

	protected void assertInvalidNullDurationInMinutes(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("durationInMinutes"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's duration must be specified"));
	}

	protected void assertInvalidZeroDurationInMinutes(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("durationInMinutes"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's duration must have at least one minute"));
	}

	protected void assertInvalidMoreThan240DurationInMinutes(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("durationInMinutes"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's duration cannot be bigger than 240 minutes"));
	}

	protected void assertInvalidNullNote(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("note"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's note must be specified"));
	}
	
	protected void assertInvalidNegativeNote(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("note"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's note cannot be negative"));
	}
	
	protected void assertInvalidMoreThanFiveNote(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("note"));
		result.andExpect(jsonPath("$.errors[0].message").value("Movie's note cannot be bigger than five"));
	}
	
	protected void assertCreatedAndMovieOutputDTO(ResultActions result) throws Exception {
		assertCreated(result);
		assertMovieOutputDTO(result);
	}
	
	protected void assertInvalidDuplicateTitle(ResultActions result) throws Exception {
		assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("title"));
		result.andExpect(jsonPath("$.errors[0].message").value("There is another movie with the same title already saved"));
	}
}
