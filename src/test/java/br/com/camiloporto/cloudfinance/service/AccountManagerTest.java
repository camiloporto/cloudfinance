package br.com.camiloporto.cloudfinance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;

public class AccountManagerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
	}
	
	@Test
	public void shouldListAllRootAccountOfAProfile() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		Profile profile = userProfileManager.signUp(p);
		
		List<Account> roots = accountManager.findRootAccounts(profile);
		int EXPECTED_ACCOUNT_COUNT = 1;
		Assert.assertEquals(roots.size(), EXPECTED_ACCOUNT_COUNT, "count of root accountes not as expected");
	}
}
