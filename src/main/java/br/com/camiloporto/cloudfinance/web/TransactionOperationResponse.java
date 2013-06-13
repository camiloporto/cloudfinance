package br.com.camiloporto.cloudfinance.web;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;

@RooJavaBean
public class TransactionOperationResponse extends AbstractOperationResponse {
	

	private AccountTransaction transaction;
	
	private List<AccountTransaction> transactions;

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

	public TransactionOperationResponse(boolean success,
			List<AccountTransaction> transactions) {
		super(success);
		this.transactions = transactions;
	}

}
