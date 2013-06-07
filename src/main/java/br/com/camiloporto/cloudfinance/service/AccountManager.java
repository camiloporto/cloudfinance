package br.com.camiloporto.cloudfinance.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooService(domainTypes = { br.com.camiloporto.cloudfinance.model.Account.class })
public interface AccountManager {
	
	AccountSystem createAccountSystemFor(Profile p);

	List<Account> findRootAccounts(Profile profile);

	AccountNode getAccountBranch(Profile profile, Long accountId);
	
	public void saveAccount(Profile profile, Account account);
}
