package br.com.camiloporto.cloudfinance.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;
import org.springframework.security.access.prepost.PreAuthorize;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooService(domainTypes = { br.com.camiloporto.cloudfinance.model.Account.class })
public interface AccountManager {
	
	AccountSystem createAccountSystemFor(Profile p);

	@Deprecated
	List<Account> findRootAccounts(Profile profile);

	@PreAuthorize("hasPermission(#accountId, 'Account.read')")
	AccountNode getAccountBranch(Profile profile, Long accountId);
	
	void saveAccount(Profile profile, Account account, AccountSystem accountSystem);

	List<Account> findAllLeavesFrom(Profile profile, Long accountId);

	List<AccountSystem> findAccountSystems(Profile profile);

	AccountSystem findAccountSystem(Long id);

}
