package br.com.camiloporto.cloudfinance.web;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class UserOperationResponse extends AbstractOperationResponse {
	
	private Long userId;

	public UserOperationResponse(boolean success) {
		super(success);
	}

}
