package br.com.camiloporto.cloudfinance.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.AccountBuilder;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.ExceptionChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

public class AccountManagerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	private Account root;
	private Account otherRoot;

	private Profile profile;
	private Profile otherProfile;
	
	private AccountSystem accountSystem;
	private AccountSystem otherAccountSystem;
	
	@BeforeMethod
	public void clearUserData() {
		cleanUserData();
		
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		profile = userProfileManager.signUp(p);
		otherProfile = userProfileManager.signUp(
				new ProfileBuilder()
					.newProfile()
					.comEmail("other@email.com")
					.comSenha("otherpass")
					.create());
		
		
		this.accountSystem = accountManager.findAccountSystems(profile).get(0);
		this.root = this.accountSystem.getRootAccount();
		
		this.otherAccountSystem = accountManager.findAccountSystems(otherProfile).get(0);
		this.otherRoot = this.otherAccountSystem.getRootAccount();
		
	}
	
	@Test
	public void shouldCreateNewAccount() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new Account(name, parentAccount);
		toSave.setDescription(desc);
		toSave.setRootAccount(root);
		accountManager.saveAccount(profile, toSave, accountSystem);
		
		Assert.assertNotNull(toSave.getId(), "did not assign account id");
		Account saved = accountManager.findAccount(toSave.getId());
		Assert.assertEquals(saved.getParentAccount().getId(), parentAccount.getId(), "father id not match");
		Assert.assertEquals(saved.getRootAccount().getId(), root.getId(), "tree id not match");
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfParentAccountNullWhenCreateNewAccount() {
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = null;
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(parentAccount)
			.belongingToTreeRootAccount(root)
			.getAccount();

		try {
			accountManager.saveAccount(profile, toSave, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED}");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionWhenTryingToCreateNewAccountUnderRootAccounts() {
		
		final String name = "Account Name";
		final String desc = "Account desc";
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(root)
			.belongingToTreeRootAccount(root)
			.getAccount();

		try {
			accountManager.saveAccount(profile, toSave, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.CREATE_ACCOUNT_UNDER_ROOT_DENIED}");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfRootAccountNullWhenCreateNewAccount() {
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = null;
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(parentAccount)
			.belongingToTreeRootAccount(root)
			.getAccount();

		try {
			accountManager.saveAccount(profile, toSave, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED}");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfParentAccountIdNullWhenCreateNewAccount() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = "Account name";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new Account(name, parentAccount);
		toSave.setRootAccount(null);
		toSave.setDescription(desc);

		try {
			accountManager.saveAccount(profile, toSave, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.TREE_ROOT_ACCOUNT_REQUIRED}");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfAccountNameNullWhenCreateNewAccount() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = null;
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(parentAccount)
			.belongingToTreeRootAccount(root)
			.getAccount();

		try {
			accountManager.saveAccount(profile, toSave, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.NAME_REQUIRED}");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfAccountNameEmptyWhenCreateNewAccount() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = "";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(parentAccount)
			.belongingToTreeRootAccount(root)
			.getAccount();

		try {
			accountManager.saveAccount(profile, toSave, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.NAME_REQUIRED}");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfAccountIdEmptyWhenListAllLeaves() {
		
		try {
			accountManager.findAllLeavesFrom(profile, null);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.TREE_ROOT_ACCOUNT_REQUIRED}");
		}
		
	}
	
	//FIXME internacionalizar mensagens de AccountManager
	@Test
	public void childrenAccountNameShouldBeUniqueOnSameAccountSystem() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(parentAccount)
			.belongingToTreeRootAccount(root)
			.getAccount();
		
		accountManager.saveAccount(profile, toSave, accountSystem);

		try {
			Account childWithRepeatedName = new AccountBuilder()
				.newAccount(name, desc)
				.withParent(parentAccount)
				.belongingToTreeRootAccount(root)
				.getAccount();
			accountManager.saveAccount(profile, childWithRepeatedName, accountSystem);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.NAME_ALREADY_EXISTS}");
		}
	}
	
	//FIXME fazer esse teste passar
	@Test
	public void shouldAddAccountsWithSameNameOnDifferentAccountSystem() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		AccountNode otherRootBranch = accountManager.getAccountBranch(otherProfile, otherRoot.getId());
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		Account otherParentAccount = otherRootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(parentAccount)
			.belongingToTreeRootAccount(root)
			.getAccount();
		
		Account toSaveSame = new AccountBuilder()
			.newAccount(name, desc)
			.withParent(otherParentAccount)
			.belongingToTreeRootAccount(otherRoot)
			.getAccount();
		
		accountManager.saveAccount(profile, toSave, accountSystem);
	
		//should not throws exception
		accountManager.saveAccount(otherProfile, toSaveSame, otherAccountSystem);
		Assert.assertNotNull(toSaveSame.getId(), "did not assign account id");
		Account saved = accountManager.findAccount(toSaveSame.getId());
		Assert.assertEquals(saved.getParentAccount().getId(), otherParentAccount.getId(), "father id not match");
		Assert.assertEquals(saved.getRootAccount().getId(), otherRoot.getId(), "tree id not match");
	}
	
	@Test
	public void shouldListAllAccountSystemsOfAProfile() {
		List<AccountSystem> roots = accountManager.findAccountSystems(profile);
		
		int EXPECTED_ACCOUNT_COUNT = 1;
		Assert.assertEquals(roots.size(), EXPECTED_ACCOUNT_COUNT, "count of root accountes not as expected");
	}
	
	@Test
	public void shouldListAllLeafAccountsOfARootAccountTree() {
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new Account(name, parentAccount);
		toSave.setDescription(desc);
		toSave.setRootAccount(root);
		accountManager.saveAccount(profile, toSave, accountSystem);
		
		List<Account> leaves = accountManager.findAllLeavesFrom(profile, root.getId());
		
		final int expectedCount = 4;
		Assert.assertEquals(leaves.size(), expectedCount, "accounts count did not match expected value");
		
	}
	
	@Test
	public void shouldGetAccountBranch() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final int EXPECTED_CHILDREN_COUNT = 4;
		Assert.assertEquals(rootBranch.getAccount().getId(), root.getId(), "root of account branch not as expected");
		Assert.assertEquals(rootBranch.getChildren().size(), EXPECTED_CHILDREN_COUNT, "children count did not match expected value");
		
	}
	
	@Test
	public void shouldGetLeafAccountBranch() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		Long leafChildId = rootBranch.getChildren().get(0).getAccount().getId();
		AccountNode leafTree = accountManager.getAccountBranch(profile, leafChildId);
		
		final int EXPECTED_CHILDREN_COUNT = 0;
		Assert.assertEquals(leafTree.getAccount().getId(), leafChildId, "root of account branch not as expected");
		Assert.assertEquals(leafTree.getChildren().size(), EXPECTED_CHILDREN_COUNT, "children count did not match expected value");
		
	}
	
	@Test
	public void shouldGetNullAccountNodeIfAccountDoNotExists() {
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, 9999L);
		Assert.assertNull(rootBranch, "rootranch should be null");
		
	}
	
	@Test
	public void shouldThrowsContraintViolationExceptionWhenGetAccountBranchWithNoAccountId() {
		
		try {
			accountManager.getAccountBranch(profile, null);
			Assert.fail("did not throw expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.accounttree.ACCOUNT_ID_REQUIRED}");
		}
	}
	
	@Test
	public void shouldThrowsConstraintViolationExceptionIfProfileNull() {
		Profile NULL_PROFILE = null;
		try {
			accountManager.findRootAccounts(NULL_PROFILE);
			Assert.fail("did not throw expected exception");
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(
						"{br.com.camiloporto.cloudfinance.accountsystem.USER_ID_REQUIRED}"
				);
		}
	}
}
