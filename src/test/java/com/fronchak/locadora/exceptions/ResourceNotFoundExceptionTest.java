package com.fronchak.locadora.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ResourceNotFoundExceptionTest {

	@Test
	public void constructorWithEntityAndIsShouldMakeCorrectlyErrorMessage() {
		ResourceNotFoundException result = new ResourceNotFoundException("Movie", "2");
		assertEquals("Entity Movie not found by ID: 2", result.getMessage());
	}
	
	@Test
	public void getErrorShouldReturnEntityNotFound() {
		String result = ResourceNotFoundException.getError();
		assertEquals("Entity not found", result);
	}
}
