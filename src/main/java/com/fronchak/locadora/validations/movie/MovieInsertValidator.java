package com.fronchak.locadora.validations.movie;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.exceptions.FieldMessage;
import com.fronchak.locadora.repositories.MovieRepository;

public class MovieInsertValidator implements ConstraintValidator<MovieInsertValid, MovieInsertDTO> {

	@Autowired
	private MovieRepository repository;
	
	@Override
	public boolean isValid(MovieInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> errors = new ArrayList<>();
		
		Movie entity = repository.findByTitle(dto.getTitle());
		if(entity != null) {
			errors.add(new FieldMessage("title", "There is another movie with the same title already saved"));
		}
		
		for (FieldMessage e : errors) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		return errors.isEmpty();
	}

}
