package br.com.camiloporto.cloudfinance.checkers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;

@Configurable
public class ProfileChecker {
	
	private static final int EXPECTED_1ST_LEVEL_ACCOUNT_COUNT = 4;

	@Autowired
	private AccountSystemRepository accountSystemRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	private Profile profile;

	private Map<String, String> firstlevelAccountNames;
	
	public ProfileChecker(Profile saved) {
		this.profile = saved;
		firstlevelAccountNames = new HashMap<String, String>();
		firstlevelAccountNames.put(Account.ASSET_NAME, Account.ASSET_NAME);
		firstlevelAccountNames.put(Account.INCOME_NAME, Account.INCOME_NAME);
		firstlevelAccountNames.put(Account.LIABILITY_NAME, Account.LIABILITY_NAME);
		firstlevelAccountNames.put(Account.OUTGOING_NAME, Account.OUTGOING_NAME);
	}

	public void checkProfileCreatedCorrectly() {
		checkIdAssigned();
		checkBasicAccountsCreated();
	}

	private void checkBasicAccountsCreated() {
		List<AccountSystem> result = accountSystemRepository.findByUserProfile(profile);
		AccountSystem accountSystem = result.get(0);
		Assert.assertNotNull(accountSystem, "account system for profile was not created");
		
		List<Account> firstLevelAccounts = accountRepository.findByParentAccount(accountSystem.getRootAccount());
		Assert.assertEquals(firstLevelAccounts.size(), EXPECTED_1ST_LEVEL_ACCOUNT_COUNT, "number of 1st level account did not match");
		for (Account account : firstLevelAccounts) {
			Assert.assertTrue(firstlevelAccountNames.containsKey(account.getName()), "1st level account '" + account.getName() + "' not found");
		}
	}

	private void checkIdAssigned() {
		Assert.assertNotNull(profile.getUserId(), "id not assigned");
	}

	public ProfileChecker assertUserNameEquals(String userName) {
		Assert.assertEquals(profile.getUserId(), userName, "userName not equals");
		return this;
	}

	public ProfileChecker assertPasswordIsEmpty() {
		Assert.assertNull(profile.getPass(), "password was not cleared");
		return this;
	}

}
