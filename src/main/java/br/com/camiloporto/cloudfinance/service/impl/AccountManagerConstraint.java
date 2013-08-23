package br.com.camiloporto.cloudfinance.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;

@RooJavaBean
@Component
@Configurable
public class AccountManagerConstraint {

	public interface CREATE_NEW_ACCOUNT {}

	public interface GET_ACCOUNT_BRANCH {}
	
	public interface PROFILE_REQUIRED {}
	
	@Autowired
	private AccountRepository accountRepository;

	@NotNull(message = "{br.com.camiloporto.cloudfinance.accountsystem.USER_ID_REQUIRED}", 
			groups = {PROFILE_REQUIRED.class, GET_ACCOUNT_BRANCH.class})
	@Valid
	private Profile profile;
	
	@NotNull(message = "{br.com.camiloporto.cloudfinance.accounttree.ACCOUNT_ID_REQUIRED}",
			groups = {GET_ACCOUNT_BRANCH.class})
	private Long accountId;
	
	@Valid
	private Account account;
	
	@AssertTrue(message = "{br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED}",
    		groups={AccountManagerConstraint.CREATE_NEW_ACCOUNT.class})
	public boolean isParentAccountIdNotNull() {
		return account != null && account.getParentAccount() != null && account.getParentAccount().getId() != null;
	}
	
	@AssertTrue(message = "{br.com.camiloporto.cloudfinance.account.TREE_ROOT_ACCOUNT_REQUIRED}",
    		groups={AccountManagerConstraint.CREATE_NEW_ACCOUNT.class})
	public boolean isTreeRootAccountIdNotNull() {
		return account != null && account.getRootAccount() != null && account.getRootAccount().getId() != null;
	}
	
	@AssertTrue(message = "{br.com.camiloporto.cloudfinance.account.NAME_ALREADY_EXISTS}", 
			groups = {CREATE_NEW_ACCOUNT.class})
	public boolean isAccountNameUniqueWithinItsSisters() {
		if(account != null && account.getName() != null){
			return accountRepository.findByName(account.getName()) == null; 
		}
		//if get here, the rule is not applicable
		return true;
	}
	
	@AssertFalse(message = "{br.com.camiloporto.cloudfinance.account.CREATE_ACCOUNT_UNDER_ROOT_DENIED}",
			groups = {CREATE_NEW_ACCOUNT.class})
	public boolean isTryingToCreateAccountUnderRootAccount() {
		boolean ret = false;
		if(isParentAccountIdNotNull()) {
			Long parentAccountId = account.getParentAccount().getId();
			Account parent = accountRepository.findOne(parentAccountId);
			ret =  isRootAccount(parent);
		}
		return ret;
	}

	private boolean isRootAccount(Account account) {
		return account.getParentAccount() == null;
	}

	public AccountManagerConstraint(Profile profile) {
		this.profile = profile;
	}
	
	public AccountManagerConstraint() {
	}

}
