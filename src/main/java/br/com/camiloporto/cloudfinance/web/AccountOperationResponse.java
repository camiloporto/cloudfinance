package br.com.camiloporto.cloudfinance.web;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;

@RooJavaBean
public class AccountOperationResponse extends AbstractOperationResponse {
	
	private Account[] rootAccounts;
	
	private AccountNode accountTree;

	public AccountOperationResponse(boolean success) {
		super(success);
	}

}
