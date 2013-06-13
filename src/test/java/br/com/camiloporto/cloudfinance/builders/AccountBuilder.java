package br.com.camiloporto.cloudfinance.builders;

import br.com.camiloporto.cloudfinance.model.Account;

public class AccountBuilder {
	
	private Account account;
	
	public AccountBuilder() {
	}
	
	public AccountBuilder newAccount(String name, String desc) {
		account = new Account();
		account.setDescription(desc);
		account.setName(name);
		return this;
	}
	
	public AccountBuilder withParent(Account parent) {
		account.setParentAccount(parent);
		return this;
	}
	
	public AccountBuilder belongingToTreeRootAccount(Account treeRootAccount) {
		account.setRootAccount(treeRootAccount);
		return this;
	}
	
	public Account getAccount() {
		return this.account;
	}

}
