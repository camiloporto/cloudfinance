package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.ExceptionChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;

public class TransactionServiceTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private TransactionManager transactionManager;
	
	private Profile profile;
	
	private Account origin;
	private Account dest;
	
	@BeforeMethod
	public void beforeTests() {
		cleanUserData();
		
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		Profile p = new ProfileBuilder()
		.newProfile()
		.comEmail(camiloporto)
		.comSenha(senha)
		.create();
		profile = userProfileManager.signUp(p);
		
		List<Account> roots = accountManager.findRootAccounts(profile);
		Account root = roots.get(0);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		origin = rootBranch.getChildren().get(0).getAccount();
		dest = rootBranch.getChildren().get(1).getAccount();
	}
	
	@Test
	public void shouldAddNewTransaction() {
		AccountTransaction saved = transactionManager.saveAccountTransaction(
				profile,
				origin.getId(), dest.getId(), new Date(), new BigDecimal("1250.25"), "transaction description");
		
		Assert.assertNotNull(saved.getId(), "did not assigned an id for new transaction");
	}
	
	@Test
	public void shouldThrowsConstraintViolationExceptionWhenOriginAccountEmpty() {
		Long nullOriginId = null;
		try {
			transactionManager.saveAccountTransaction(
					profile,
					nullOriginId, 
					dest.getId(), 
					new Date(), 
					new BigDecimal("1250.25"), 
					"transaction description");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("br.com.camiloporto.cloudfinance.transaction.ORIGIN_ACCOUNT_REQUIRED");
		}
	}
	
	@Test
	public void shouldThrowsConstraintViolationExceptionWhenDestAccountEmpty() {
		Long nullDestId = null;
		try {
			transactionManager.saveAccountTransaction(
					profile,
					origin.getId(), 
					nullDestId, 
					new Date(), 
					new BigDecimal("1250.25"), 
					"transaction description");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("br.com.camiloporto.cloudfinance.transaction.DEST_ACCOUNT_REQUIRED");
		}
	}
	
	@Test(dataProvider = "transactionValidationDataProvider")
	public void validateSaveTransactionInputs(Profile profile, Long originAccountId, Long destAccountId, 
			Date transactionDate, BigDecimal amount, String description, String expectedExceptionMessage) {
		try {
			transactionManager.saveAccountTransaction(
					profile,
					originAccountId, 
					destAccountId, 
					transactionDate, 
					amount, 
					description);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(expectedExceptionMessage);
		}
	}
	
	@DataProvider(name = "transactionValidationDataProvider")
	public Iterator<Object[]> saveTransactionValidationTestData() {
		beforeTests();
		
		//FIXME adicionar testes de validacao de entradas para transacao
		List<Object[]> testData = new ArrayList<Object[]>();
		
		testData.add(new Object[] {profile, null, dest.getId(), new Date(), new BigDecimal("1250.25"), "transact description", "br.com.camiloporto.cloudfinance.transaction.ORIGIN_ACCOUNT_REQUIRED"});
		testData.add(new Object[] {profile, origin.getId(), null, new Date(), new BigDecimal("1250.25"), "transact description", "br.com.camiloporto.cloudfinance.transaction.DEST_ACCOUNT_REQUIRED"});
		testData.add(new Object[] {profile, origin.getId(), dest.getId(), null, new BigDecimal("1250.25"), "transact description", "br.com.camiloporto.cloudfinance.transaction.DATE_REQUIRED"});
		testData.add(new Object[] {profile, origin.getId(), dest.getId(), new Date(), null, "transact description", "br.com.camiloporto.cloudfinance.transaction.AMOUNT_REQUIRED"});
		
		return testData.iterator();
	}
	
	
}
