package br.com.camiloporto.cloudfinance.checkers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;

@Configurable
public class ProfileChecker {
	
	@Autowired
	private AccountSystemRepository accountSystemRepository;

	private Profile profile;

	public ProfileChecker(Profile saved) {
		this.profile = saved;
	}

	public void checkProfileCreatedCorrectly() {
		checkIdAssigned();
		checkBasicAccountsCreated();
	}

	private void checkBasicAccountsCreated() {
		AccountSystem accountSystem = accountSystemRepository.findByUserProfile(profile);
		Assert.assertNotNull(accountSystem, "account system for profile was not created");
	}

	private void checkIdAssigned() {
		Assert.assertNotNull(profile.getId(), "id not assigned");
	}

}
