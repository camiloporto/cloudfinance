package br.com.camiloporto.cloudfinance.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.ExceptionChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
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
	public void shouldCreateNewAccount() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		Profile profile = userProfileManager.signUp(p);
		List<Account> roots = accountManager.findRootAccounts(profile);
		Account root = roots.get(0);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = rootBranch.getChildren().get(0).getAccount();
		
		Account toSave = new Account(name, parentAccount);
		toSave.setDescription(desc);
		accountManager.saveAccount(toSave);
		
		Assert.assertNotNull(toSave.getId(), "did not assign account id");
		Account saved = accountManager.findAccount(toSave.getId());
		Assert.assertEquals(saved.getParentAccount().getId(), parentAccount.getId(), "father id not match");
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfParentAccountNullWhenCreateNewAccount() {
		
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		Profile profile = userProfileManager.signUp(p);
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = null;
		
		Account toSave = new Account(name, parentAccount);
		toSave.setDescription(desc);

		try {
			accountManager.saveAccount(profile, toSave);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED");
		}
		
	}
	
	@Test
	public void shouldThrowConstraintViolationExceptionIfParentAccountIdNullWhenCreateNewAccount() {
		
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		Profile profile = userProfileManager.signUp(p);
		
		final String name = "Account Name";
		final String desc = "Account desc";
		Account parentAccount = new Account();//no ID assigned
		
		Account toSave = new Account(name, parentAccount);
		toSave.setDescription(desc);

		try {
			accountManager.saveAccount(profile, toSave);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED");
		}
		
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
	
	@Test
	public void shouldGetAccountBranch() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		Profile profile = userProfileManager.signUp(p);
		List<Account> roots = accountManager.findRootAccounts(profile);
		Account root = roots.get(0);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		
		final int EXPECTED_CHILDREN_COUNT = 4;
		Assert.assertEquals(rootBranch.getAccount().getId(), root.getId(), "root of account branch not as expected");
		Assert.assertEquals(rootBranch.getChildren().size(), EXPECTED_CHILDREN_COUNT, "children count did not match expected value");
		
	}
	
	@Test
	public void shouldGetLeafAccountBranch() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		Profile profile = userProfileManager.signUp(p);
		List<Account> roots = accountManager.findRootAccounts(profile);
		Account root = roots.get(0);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		Long leafChildId = rootBranch.getChildren().get(0).getAccount().getId();
		AccountNode leafTree = accountManager.getAccountBranch(profile, leafChildId);
		
		final int EXPECTED_CHILDREN_COUNT = 0;
		Assert.assertEquals(leafTree.getAccount().getId(), leafChildId, "root of account branch not as expected");
		Assert.assertEquals(leafTree.getChildren().size(), EXPECTED_CHILDREN_COUNT, "children count did not match expected value");
		
	}
	
	@Test
	public void shouldGetNullAccountNodeIfAccountDoNotExists() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		Profile profile = userProfileManager.signUp(p);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, 9999L);
		Assert.assertNull(rootBranch, "rootranch should be null");
		
	}
	
	@Test
	public void shouldThrowsContraintViolationExceptionWhenGetAccountBranchWithNoAccountId() {
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		
		Profile profile = userProfileManager.signUp(p);
		
		try {
			accountManager.getAccountBranch(profile, null);
			Assert.fail("did not throw expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("br.com.camiloporto.cloudfinance.accounttree.ACCOUNT_ID_REQUIRED");
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
						"br.com.camiloporto.cloudfinance.accountsystem.USER_ID_REQUIRED"
				);
		}
	}
}
