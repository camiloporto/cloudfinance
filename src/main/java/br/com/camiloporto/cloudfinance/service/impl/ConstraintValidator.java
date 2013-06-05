package br.com.camiloporto.cloudfinance.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class ConstraintValidator<EntryType> {

	@Autowired
	private Validator validator;

	public ConstraintValidator() {
		super();
	}

	public void validateForGroups(EntryType entryToValidate, Class<?>...groups)
			throws ConstraintViolationException {
				Set<ConstraintViolation<EntryType>> violations = validator.validate(entryToValidate, groups);
				throwNewConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}

	private void throwNewConstraintViolationException(HashSet<ConstraintViolation<?>> violations) {
		if(!violations.isEmpty()) {
			throw 
				new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

}