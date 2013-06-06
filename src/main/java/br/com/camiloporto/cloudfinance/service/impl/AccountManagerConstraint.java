package br.com.camiloporto.cloudfinance.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Profile;

@RooJavaBean
public class AccountManagerConstraint {

	public interface GET_ACCOUNT_BRANCH {}
	
	public interface PROFILE_REQUIRED {}

	@NotNull(message = "br.com.camiloporto.cloudfinance.accountsystem.USER_ID_REQUIRED", 
			groups = {PROFILE_REQUIRED.class, GET_ACCOUNT_BRANCH.class})
	@Valid
	private Profile profile;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.accounttree.ACCOUNT_ID_REQUIRED",
			groups = {GET_ACCOUNT_BRANCH.class})
	private Long accountId;

	public AccountManagerConstraint(Profile profile) {
		this.profile = profile;
	}

}
