package br.com.camiloporto.cloudfinance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import br.com.camiloporto.cloudfinance.service.AccountManager;

public class AccountManagerImpl implements AccountManager {
	
	@Autowired
	private AccountSystemRepository accountSystemRepository;
	
	@Override
	public List<Account> findRootAccounts(Profile profile) {
		checkProfileRequired(profile);
		List<AccountSystem> accountSystems = accountSystemRepository.findByUserProfile(profile);
		List<Account> roots = createRootAccountList(accountSystems);
		return roots;
	}
	
	@Override
	public AccountNode getAccountBranch(Profile profile, Long accountId) {
		checkGetAccountBranchEntries(profile, accountId);
		AccountNode root = getNodeAccount(accountId);
		appendBranchTree(root);
		return root;
	}

	private void checkGetAccountBranchEntries(Profile profile, Long accountId) {
		AccountManagerConstraint constraints = new AccountManagerConstraint(profile);
		constraints.setAccountId(accountId);
		
		new ConstraintValidator<AccountManagerConstraint>()
			.validateForGroups(constraints,
				AccountManagerConstraint.GET_ACCOUNT_BRANCH.class);
	}

	private void appendBranchTree(AccountNode node) {
		List<Account> childrenAccounts = accountRepository.findByParentAccount(node.getAccount());
		for (Account account : childrenAccounts) {
			AccountNode childNode = new AccountNode(account);
			node.getChildren().add(childNode);
			appendBranchTree(childNode);
		}
	}

	private AccountNode getNodeAccount(Long accountId) {
		Account nodeRootAccount = accountRepository.findOne(accountId);
		return new AccountNode(nodeRootAccount);
	}
	
	private void checkProfileRequired(Profile profile) {
		new ConstraintValidator<AccountManagerConstraint>()
			.validateForGroups(new AccountManagerConstraint(profile),
					AccountManagerConstraint.PROFILE_REQUIRED.class);
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
