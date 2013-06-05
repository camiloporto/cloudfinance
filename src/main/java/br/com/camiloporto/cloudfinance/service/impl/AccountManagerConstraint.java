package br.com.camiloporto.cloudfinance.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.camiloporto.cloudfinance.model.Profile;

public class AccountManagerConstraint {

	@NotNull(message = "br.com.camiloporto.cloudfinance.accountsystem.USER_ID_REQUIRED", groups = {PROFILE_REQUIRED.class})
	@Valid
	private Profile profile;

	public interface PROFILE_REQUIRED {}

	public AccountManagerConstraint(Profile profile) {
		this.profile = profile;
	}

}
