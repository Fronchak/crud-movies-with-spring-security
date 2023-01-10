package com.fronchak.locadora.controllers.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.locadora.services.UserService;
import com.fronchak.locadora.util.CustomizeControllerAsserts;
import com.fronchak.locadora.util.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractUserControllerTest {

	protected static final Long EXISTING_ID = 1L;
	protected static final Long NON_EXISTING_ID = 2L;
	protected static final Long DEPENDENT_ID = 3L;
	
	private static final String CLIENT_USERNAME = "gmack@gmail.com";
	private static final String CLIENT_PASSWORD = "123456";
	private static final String OPERATOR_USERNAME = "gabriel@gmail.com";
	private static final String OPERATOR_PASSWORD = "123456";
	private static final String ADMIN_USERNAME = "fronchak@gmail.com";
	private static final String ADMIN_PASSWORD = "123456";
	
	protected static final String USED_EMAIL = "fronchak@gmail.com";
	protected static final String NOT_USED_EMAIL = "gmack123@gmail.com";
	
	protected String accessToken;
	
	protected static MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected ObjectMapper mapper;
	
	@Autowired
	protected TokenUtil tokenUtil;
	
	//@MockBean
	@SpyBean
	protected UserService service;
	
	protected void getClientToken() throws Exception {
		accessToken = tokenUtil.obtainAccessToken(mockMvc, CLIENT_USERNAME, CLIENT_PASSWORD);
	}
	
	protected void getOperatorToken() throws Exception {
		accessToken = tokenUtil.obtainAccessToken(mockMvc, OPERATOR_USERNAME, OPERATOR_PASSWORD);
	}
	
	protected void getAdminToken() throws Exception {
		accessToken = tokenUtil.obtainAccessToken(mockMvc, ADMIN_USERNAME, ADMIN_PASSWORD);
	}
	
	protected void assertSuccessAndOutputDTO(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertSuccess(result);
		assertOutputDTO(result);
	}
	
	protected void assertOutputDTO(ResultActions result) throws Exception {
		result.andExpect(jsonPath("$.id").value(30L));
		result.andExpect(jsonPath("$.email").value("mock_email_0@gmail.com.br"));
		result.andExpect(jsonPath("$.roles[0].id").value(20L));
		result.andExpect(jsonPath("$.roles[0].authority").value("ROLE_AUTHORITY_0"));
		result.andExpect(jsonPath("$.roles[1].id").value(21L));
		result.andExpect(jsonPath("$.roles[1].authority").value("ROLE_AUTHORITY_1"));
	}
	
	protected void assertInvalidNullEmail(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's email must be specified"));
	}
	
	protected void assertInvalidNotWellFormatedEmail(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("Invalid email format, please try a valid email"));
	}
	
	protected void assertInvalidNullPassword(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("password"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's password cannot be empty"));
	}
	
	protected void assertInvalidEmptyPassword(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("password"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's password cannot be empty"));
	}
	
	protected void assertInvalidLessThan6LettersPassword(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("password"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's password must have at least 6 letters"));
	}
	
	protected void assertInvalidNullOldPassword(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("oldPassword"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's old password cannot be empty"));
	}
	
	protected void assertInvalidEmptyOldPassword(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("oldPassword"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's old password cannot be empty"));
	}
	
	protected void assertInvalidLessThan6LettersOldPassword(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("oldPassword"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's old password must have at least 6 letters"));
	}
	
	protected void assertInvalidNullRoles(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("roles"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's roles cannot be empty, must have at least one role"));
	}
	
	protected void assertInvalidEmptyRoles(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("roles"));
		result.andExpect(jsonPath("$.errors[0].message").value("User's roles cannot be empty, must have at least one role"));
	}
	
	protected void assertInvalidNullRole(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].message").value("User's role must be specified"));
	}
	
	protected void assertCreatedAndOutputDTO(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertCreated(result);
		assertOutputDTO(result);
	}
	
	protected void assertInvalidPasswordException(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertBadRequest(result);
		result.andExpect(jsonPath("$.error").value("invalid_grant"));
	}
	
	protected void assertInvalidDuplicateEmail(ResultActions result) throws Exception {
		CustomizeControllerAsserts.assertUnprocessableEntity(result);
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("Email already been used, please choose another one"));
	}
}
