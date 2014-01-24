package br.com.camiloporto.cloudfinance.web;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAdviceController {
	
	private static Logger logger = Logger.getLogger(ExceptionHandlerAdviceController.class);
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
	public void handleAccessDenied() {
		
	}
	
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleThrowable(Throwable e) {
		logger.error("error", e);
	}

}
