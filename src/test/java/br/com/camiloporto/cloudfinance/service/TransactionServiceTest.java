package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.mockito.Mockito;
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
import br.com.camiloporto.cloudfinance.service.impl.TransactionManagerImpl;

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
	
	private Clock fakeClock = Mockito.mock(Clock.class);
	
	@BeforeMethod
	public void beforeTests() {
		cleanUserData();
		((TransactionManagerImpl)transactionManager).setClock(fakeClock);
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
	
	@Test
	public void shouldSetDefaultBeginDateWhenItNotInformed() {
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
		
		//assuming today is 13/06/2013
		Calendar fakeToday = new GregorianCalendar(2013, Calendar.JUNE, 13);
		Mockito.when(fakeClock.today()).thenReturn(fakeToday);
		
		Date beginDate = null;
		List<AccountTransaction> result = 
				transactionManager.findAccountTransactionByDateBetween(
						profile, 
						root.getId(), 
						beginDate, d3.getTime());

		//expected default begin date should be 3 days before 'today'
		int expectedResultCount = 3;
		Assert.assertEquals(result.size(), expectedResultCount, "result count did not match");
		new TransactionTestChecker()
			.assertThatTransactionsArePresent(result, "t1", "t2", "t3");
	}
	
	@Test
	public void shouldSetDefaultEndDateWhenItNotInformed() {
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
		
		//assuming today is 13/06/2013
		Calendar fakeToday = new GregorianCalendar(2013, Calendar.JUNE, 13);
		Mockito.when(fakeClock.today()).thenReturn(fakeToday);
		
		Date endDate = null;
		List<AccountTransaction> result = 
				transactionManager.findAccountTransactionByDateBetween(
						profile, 
						root.getId(), 
						d1.getTime(), endDate);
		
		//expected default end date should be 'today'.
		int expectedResultCount = 2;
		Assert.assertEquals(result.size(), expectedResultCount, "result count did not match");
		new TransactionTestChecker()
			.assertThatTransactionsArePresent(result, "t1", "t2");
	}
	
	@Test(dataProvider = "txFindByDate.invalidInputs")
	public void shouldThrowsExceptionWhenInvalidInputOnFindTransactionBetweenDates(
			Profile profile, Long rootAccountId, Date begin, Date end, String expectedErrorMessage) {
		
		try {
			transactionManager.findAccountTransactionByDateBetween(
					profile, 
					rootAccountId, 
					begin, 
					end);
			Assert.fail("did not throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(expectedErrorMessage);
		}
	}
	
	@DataProvider(name = "txFindByDate.invalidInputs")
	public Iterator<Object[]> findTransactionByDateValidationTestData() {
		beforeTests();
		Calendar begin = new GregorianCalendar(2013, Calendar.JUNE, 12);
		Calendar end = new GregorianCalendar(2013, Calendar.JUNE, 14);
		
		List<Object[]> testData = new ArrayList<Object[]>();
		
		//no profile
		testData.add(new Object[] {null, root.getId(), begin.getTime(), end.getTime(), "br.com.camiloporto.cloudfinance.user.LOGGED_USER_REQUIRED"});
		
		//no rootAccount id informed
		testData.add(new Object[] {profile, null, begin.getTime(), end.getTime(), "br.com.camiloporto.cloudfinance.account.TREE_ROOT_ACCOUNT_REQUIRED"});
		
		//begin date > end date
		testData.add(new Object[] {profile, root.getId(), end.getTime(), begin.getTime(), "br.com.camiloporto.cloudfinance.transaction.BEGIN_DATE_GREATER_THAN_END_DATE"});
		
		return testData.iterator();
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
