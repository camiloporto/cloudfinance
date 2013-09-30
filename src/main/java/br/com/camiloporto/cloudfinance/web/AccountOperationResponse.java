package br.com.camiloporto.cloudfinance.web;

import javax.validation.ConstraintViolationException;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountSystem;

@RooJavaBean
public class AccountOperationResponse extends AbstractOperationResponse {
	
	private Account[] rootAccounts;
	
	private AccountSystem[] accountSystems;
	
	private Account[] leafAccounts;
	
	private AccountNode accountTree;
	
	private Account account;
	
	private AccountSystem accountSystem;

	public AccountOperationResponse(boolean success) {
		super(success);
	}

	public AccountOperationResponse(ConstraintViolationException e) {
		super(e);
	}

}
