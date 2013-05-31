package br.com.camiloporto.cloudfinance.web;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public abstract class AbstractOperationResponse {
	
	private boolean success;
	
	public AbstractOperationResponse() {
	}
	
	public AbstractOperationResponse(boolean success) {
		this.success = success;
	}

	
	
}
