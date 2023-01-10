package com.fronchak.locadora.validations.user;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.fronchak.locadora.dtos.user.UserInsertDTO;
import com.fronchak.locadora.entities.User;
import com.fronchak.locadora.exceptions.FieldMessage;
import com.fronchak.locadora.repositories.UserRepository;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> errors = new ArrayList<>();
		
		User user = repository.findByEmail(dto.getEmail());
		if(user != null) {
			errors.add(new FieldMessage("email", "Email already been used, please choose another one"));
		}
		
		for (FieldMessage e : errors) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		return errors.isEmpty();
	}
}


