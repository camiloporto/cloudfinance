package br.com.camiloporto.cloudfinance.web;

import javax.validation.ConstraintViolationException;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;

@RooJavaBean
public class TransactionOperationResponse extends AbstractOperationResponse {
	

	private AccountTransaction transaction;

	public TransactionOperationResponse(boolean success) {
		super(success);
	}

	public TransactionOperationResponse(boolean success,
			AccountTransaction transaction) {
		super(success);
		this.transaction = transaction;
		
	}

	public TransactionOperationResponse(ConstraintViolationException e) {
		super(e);
	}

}
