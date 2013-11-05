package br.com.camiloporto.cloudfinance.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAdviceController {
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
	public void handleAccessDenied() {
		
	}

}
