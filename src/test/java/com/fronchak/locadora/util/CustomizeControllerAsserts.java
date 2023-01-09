package com.fronchak.locadora.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultActions;

public class CustomizeControllerAsserts {

	public static void assertUnauthorized(ResultActions result) throws Exception {
		result.andExpect(status().isUnauthorized());
		result.andExpect(jsonPath("$.error").value("unauthorized"));
	}
	
	public static void assertSuccess(ResultActions result) throws Exception {
		result.andExpect(status().isOk());
	}

	public static void assertNotFound(ResultActions result) throws Exception {
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Entity not found"));
	}

	public static void assertForbidden(ResultActions result) throws Exception {
		result.andExpect(status().isForbidden());
		result.andExpect(jsonPath("$.error").value("access_denied"));
	}

	public static void assertUnprocessableEntity(ResultActions result) throws Exception {
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.error").value("Validation error"));
	}
	
	public static void assertCreated(ResultActions result) throws Exception {
		result.andExpect(status().isCreated());
	}

	public static void assertNoContent(ResultActions result) throws Exception {
		result.andExpect(status().isNoContent());
	}
	
	public static void assertBadRequestAndDatabaseException(ResultActions result) throws Exception {
		assertBadRequest(result);
		result.andExpect(jsonPath("$.error").value("Integrity error"));
	}
	
	public static void assertBadRequest(ResultActions result) throws Exception {
		result.andExpect(status().isBadRequest());
	}
}
