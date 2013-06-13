package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import br.com.camiloporto.cloudfinance.checkers.TransactionTestChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountEntry;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountEntryRepository;

public class TransactionServiceTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private AccountEntryRepository accountEntryRepository;
	
	private Profile profile;
	
	private Account origin;
	private Account dest;
	private Account root;
	
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
		root = roots.get(0);
		
		AccountNode rootBranch = accountManager.getAccountBranch(profile, root.getId());
		origin = rootBranch.getChildren().get(0).getAccount();
		dest = rootBranch.getChildren().get(1).getAccount();
	}
	
	@Test
	public void shouldAddNewTransaction() {
		BigDecimal amount = new BigDecimal("1250.25");
		AccountTransaction saved = transactionManager.saveAccountTransaction(
				profile,
				origin.getId(), dest.getId(), new Date(), amount, "transaction description");
		
		Assert.assertNotNull(saved.getId(), "did not assigned an id for new transaction");
		assertAccountEntryAmountWasRegistered(origin, amount.negate());
		assertAccountEntryAmountWasRegistered(dest, amount);
	}
	
	private void assertAccountEntryAmountWasRegistered(Account account,
			BigDecimal amount) {
		List<AccountEntry> entries = accountEntryRepository.findAll();
		for (AccountEntry accountEntry : entries) {
			if(accountEntry.getAccount().getId().equals(account.getId())) {
				Assert.assertEquals(accountEntry.getEntryValue(), amount, "expected accountEntry amount not match");
			}
		}
	}
	
	@Test
	public void shouldGetTransactionBetweenDates() {
		Calendar d1 = new GregorianCalendar(2013, Calendar.JUNE, 10);
		Calendar d2 = new GregorianCalendar(2013, Calendar.JUNE, 12);
		Calendar d3 = new GregorianCalendar(2013, Calendar.JUNE, 14);
		Calendar d4 = new GregorianCalendar(2013, Calendar.JUNE, 16);
		
		transactionManager.saveAccountTransaction(
				profile,
				origin.getId(), 
				dest.getId(), 
				d1.getTime(), new BigDecimal("1234.50"), 
				"t1");
		
		transactionManager.saveAccountTransaction(
				profile,
				origin.getId(), 
				dest.getId(), 
				d2.getTime(), new BigDecimal("1234.50"), 
				"t2");
		transactionManager.saveAccountTransaction(
				profile,
				origin.getId(), 
				dest.getId(), 
				d3.getTime(), new BigDecimal("1234.50"), 
				"t3");
		transactionManager.saveAccountTransaction(
				profile,
				origin.getId(), 
				dest.getId(), 
				d4.getTime(), new BigDecimal("1234.50"), 
				"t4");
		
		List<AccountTransaction> result = 
				transactionManager.findAccountTransactionByDateBetween(
						profile, 
						root.getId(), 
						d2.getTime(), d3.getTime());
		
		int expectedResultCount = 2;
		Assert.assertEquals(result.size(), expectedResultCount, "result count did not match");
		new TransactionTestChecker()
			.assertThatTransactionsArePresent(result, "t2", "t3");
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
		
		List<Object[]> testData = new ArrayList<Object[]>();
		
		testData.add(new Object[] {profile, null, dest.getId(), new Date(), new BigDecimal("1250.25"), "transact description", "br.com.camiloporto.cloudfinance.transaction.ORIGIN_ACCOUNT_REQUIRED"});
		testData.add(new Object[] {profile, origin.getId(), null, new Date(), new BigDecimal("1250.25"), "transact description", "br.com.camiloporto.cloudfinance.transaction.DEST_ACCOUNT_REQUIRED"});
		testData.add(new Object[] {profile, origin.getId(), dest.getId(), null, new BigDecimal("1250.25"), "transact description", "br.com.camiloporto.cloudfinance.transaction.DATE_REQUIRED"});
		testData.add(new Object[] {profile, origin.getId(), dest.getId(), new Date(), null, "transact description", "br.com.camiloporto.cloudfinance.transaction.AMOUNT_REQUIRED"});
		
		return testData.iterator();
	}
	
	
}
