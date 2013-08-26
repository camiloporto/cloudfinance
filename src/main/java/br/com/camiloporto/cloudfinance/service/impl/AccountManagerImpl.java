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
	
	public List<Account> findAllLeavesFrom(Profile profile, Long rootAccountId) {
		checkFindAllLeavesEntries(profile, rootAccountId);
		return accountRepository.findLeavesFrom(rootAccountId);
	}
	
	private void checkFindAllLeavesEntries(Profile profile, Long accountId) {
		AccountManagerConstraint constraints = new AccountManagerConstraint();
		Account account = new Account();
		Account root = new Account();
		root.setId(accountId);
		account.setRootAccount(root);
		constraints.setAccount(account);
		constraints.setProfile(profile);
		
		new ConstraintValidator<AccountManagerConstraint>()
			.validateForGroups(constraints,
				AccountManagerConstraint.FIND_LEAVES_ACCOUNTS.class);
	}

	public void saveAccount(Profile profile, Account account) {
		checkCreateNewAccountEntries(profile, account);
		Account parent = accountRepository.findOne(account.getParentAccount().getId());
		Account treeRoot = accountRepository.findOne(account.getRootAccount().getId());
		account.setParentAccount(parent);
		account.setRootAccount(treeRoot);
		accountRepository.save(account);
	}
	
	private void checkCreateNewAccountEntries(Profile profile, Account account) {
		AccountManagerConstraint constraints = new AccountManagerConstraint();
		constraints.setAccount(account);
		constraints.setProfile(profile);
		
		new ConstraintValidator<AccountManagerConstraint>()
			.validateForGroups(constraints,
				AccountManagerConstraint.CREATE_NEW_ACCOUNT.class);
	}

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
		if(root != null) {
			appendBranchTree(root);
		}
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
		if(nodeRootAccount != null) {
			return new AccountNode(nodeRootAccount);
		}
		return null;
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
		asset.setRootAccount(root);
		Account liability = new Account(Account.LIABILITY_NAME, root);
		liability.setRootAccount(root);
		Account income = new Account(Account.INCOME_NAME, root);
		income.setRootAccount(root);
		Account outgoing = new Account(Account.OUTGOING_NAME, root);
		outgoing.setRootAccount(root);
		
		accountRepository.save(asset);
		accountRepository.save(liability);
		accountRepository.save(income);
		accountRepository.save(outgoing);
		
		
		return root;
	}
}
