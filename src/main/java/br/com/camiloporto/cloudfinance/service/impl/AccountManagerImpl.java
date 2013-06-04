package br.com.camiloporto.cloudfinance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import br.com.camiloporto.cloudfinance.service.AccountManager;

public class AccountManagerImpl implements AccountManager {
	
	@Autowired
	private AccountSystemRepository accountSystemRepository;
	
	@Override
	public List<Account> findRootAccounts(Profile profile) {
		List<AccountSystem> accountSystems = accountSystemRepository.findByUserProfile(profile);
		List<Account> roots = createRootAccountList(accountSystems);
		return roots;
	}
	
	private List<Account> createRootAccountList(
			List<AccountSystem> accountSystems) {
		List<Account> result = new ArrayList<Account>();
		for (AccountSystem as : accountSystems) {
			result.add(as.getRootAccount());
		}
		return result;
	}

	@Override
	public AccountSystem createAccountSystemFor(Profile p) {
		AccountSystem as = new AccountSystem();
		as.setUserProfile(p);
		Account rootAccount = createBasicAccountTree(p);
		as.setRootAccount(rootAccount);
		
		return accountSystemRepository.save(as);
	}

	private Account createBasicAccountTree(Profile p) {
		Account root = new Account();
		root.setName(p.getUserId());
		accountRepository.save(root);
		
		Account asset = new Account(Account.ASSET_NAME, root);
		Account liability = new Account(Account.LIABILITY_NAME, root);
		Account income = new Account(Account.INCOME_NAME, root);
		Account outgoing = new Account(Account.OUTGOING_NAME, root);
		
		accountRepository.save(asset);
		accountRepository.save(liability);
		accountRepository.save(income);
		accountRepository.save(outgoing);
		
		
		return root;
	}
}
