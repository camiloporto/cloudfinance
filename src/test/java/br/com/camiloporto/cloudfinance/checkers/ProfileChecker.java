package br.com.camiloporto.cloudfinance.checkers;

import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.Profile;

public class ProfileChecker {

	private Profile profile;

	public ProfileChecker(Profile saved) {
		this.profile = saved;
	}

	public void checkProfileCreatedCorrectly() {
		checkIdAssigned();
	}

	private void checkIdAssigned() {
		Assert.assertNotNull(profile.getId(), "id not assigned");
	}

}
