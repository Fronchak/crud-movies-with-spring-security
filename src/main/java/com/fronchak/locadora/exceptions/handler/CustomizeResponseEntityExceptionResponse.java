package com.fronchak.locadora.exceptions.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fronchak.locadora.exceptions.DatabaseException;
import com.fronchak.locadora.exceptions.ExceptionResponse;
import com.fronchak.locadora.exceptions.InvalidPasswordException;
import com.fronchak.locadora.exceptions.OAuthCustomError;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.exceptions.ValidationExceptionResponse;

@RestControllerAdvice
public class CustomizeResponseEntityExceptionResponse {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ExceptionResponse response = makeResponse(
				new ExceptionResponse(), e, request, status, ResourceNotFoundException.getError());
		return ResponseEntity.status(status).body(response);
	}
	
	private ExceptionResponse makeResponse(
			ExceptionResponse response, Exception e, WebRequest request, HttpStatus status, String error) {
		response.setTimestamp(Instant.now());
		response.setStatus(status.value());
		response.setError(error);
		response.setMessage(e.getMessage());
		response.setPath(request.getDescription(false));
		return response;
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ExceptionResponse> handleDatabaseException(DatabaseException e, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ExceptionResponse response = makeResponse(new ExceptionResponse(), e, request, status, DatabaseException.getError());
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e, WebRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationExceptionResponse response = (ValidationExceptionResponse) makeResponse(
				new ValidationExceptionResponse(), e, request, status, "Validation error");	
		
		for(FieldError error : e.getBindingResult().getFieldErrors()) {
			response.addError(error.getField(), error.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ExceptionResponse response = makeResponse(new ExceptionResponse(), e, request, status, "Internal Server Error");
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<OAuthCustomError> handleInvalidPasswordException(InvalidPasswordException e, WebRequest request) {
		OAuthCustomError response = new OAuthCustomError();
		response.setError(InvalidPasswordException.getError());
		response.setErrorDescription(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
