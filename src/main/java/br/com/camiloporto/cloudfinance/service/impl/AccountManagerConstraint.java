package br.com.camiloporto.cloudfinance.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooJavaBean
public class AccountManagerConstraint {

	public interface CREATE_NEW_ACCOUNT {}

	public interface GET_ACCOUNT_BRANCH {}
	
	public interface PROFILE_REQUIRED {}

	@NotNull(message = "br.com.camiloporto.cloudfinance.accountsystem.USER_ID_REQUIRED", 
			groups = {PROFILE_REQUIRED.class, GET_ACCOUNT_BRANCH.class})
	@Valid
	private Profile profile;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.accounttree.ACCOUNT_ID_REQUIRED",
			groups = {GET_ACCOUNT_BRANCH.class})
	private Long accountId;
	
	@Valid
	private Account account;
	
	@AssertTrue(message = "br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED",
    		groups={AccountManagerConstraint.CREATE_NEW_ACCOUNT.class})
	public boolean isParentAccountIdNotNull() {
		return account != null && account.getParentAccount() != null && account.getParentAccount().getId() != null;
	}

	public AccountManagerConstraint(Profile profile) {
		this.profile = profile;
	}

}
