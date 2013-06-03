package br.com.camiloporto.cloudfinance.web;

import javax.validation.ConstraintViolationException;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class UserOperationResponse extends AbstractOperationResponse {
	
	private Long userId;

	public UserOperationResponse(boolean success) {
		super(success);
	}

	public UserOperationResponse(ConstraintViolationException e) {
		super(e);
	}

}
