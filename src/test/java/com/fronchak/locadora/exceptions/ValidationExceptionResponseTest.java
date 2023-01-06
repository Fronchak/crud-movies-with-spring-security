package com.fronchak.locadora.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ValidationExceptionResponseTest {

	private ValidationExceptionResponse response;
	
	@BeforeEach
	public void setUp() {
		response = new ValidationExceptionResponse();
	}
	
	@Test
	public void addErrorShouldAddErrorToErrorsList() {
		String fieldName = "Name";
		String message = "Name cannot be empty";
		FieldMessage fieldMessage = new FieldMessage(fieldName, message);
		
		response.addError(fieldMessage);
		
		FieldMessage result = response.getErrors().get(0);
		assertEquals(fieldName, result.getFieldName());
		assertEquals(message, result.getMessage());
		assertEquals(1, response.getErrors().size());
	}
	
	@Test
	public void addErrorShouldCreateAndAddErrorToErrorsList() {
		String fieldName = "Name";
		String message = "Name cannot be empty";
		
		response.addError(fieldName, message);
		
		FieldMessage result = response.getErrors().get(0);
		assertEquals(fieldName, result.getFieldName());
		assertEquals(message, result.getMessage());
		assertEquals(1, response.getErrors().size());
	}
}
