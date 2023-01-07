package com.fronchak.locadora.validations.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.fronchak.locadora.dtos.movie.MovieUpdateDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.exceptions.FieldMessage;
import com.fronchak.locadora.repositories.MovieRepository;

public class MovieUpdateValidator implements ConstraintValidator<MovieUpdateValid, MovieUpdateDTO> {

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public boolean isValid(MovieUpdateDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> errors = new ArrayList<>();
		
		Map<String, String> uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Long id = Long.parseLong(uriVars.get("id"));
		
		Movie entity = repository.findByTitle(dto.getTitle());
		if(entity != null && !entity.getId().equals(id)) {
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
