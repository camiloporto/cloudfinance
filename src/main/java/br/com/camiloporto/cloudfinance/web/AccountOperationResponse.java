package br.com.camiloporto.cloudfinance.web;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;

@RooJavaBean
public class AccountOperationResponse extends AbstractOperationResponse {
	
	private Account[] rootAccounts = new Account[]{};

	public AccountOperationResponse(boolean success) {
		super(success);
	}

}
