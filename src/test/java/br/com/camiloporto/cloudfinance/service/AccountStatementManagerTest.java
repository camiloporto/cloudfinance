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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.AccountStatementChecker;
import br.com.camiloporto.cloudfinance.checkers.ExceptionChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatementManagerImpl;
import br.com.camiloporto.cloudfinance.service.impl.TransactionManagerImpl;

public class AccountStatementManagerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Autowired
	private AccountManager accountManager;
	
	private Profile profile;
	private Account incomes;
	private Account bank;
	private Account outgoings;
	
	private Clock fakeClock = Mockito.mock(Clock.class);
	
	private Account root;

	@Autowired
	private AccountStatementManager accountStatementService;

	private Calendar tx01Date;

	private Calendar tx02Date;

	private Calendar tx03Date;

	private Calendar tx04Date;

	private BigDecimal tx01Amount;

	private BigDecimal tx02Amount;

	private BigDecimal tx03Amount;

	private BigDecimal tx04Amount;
	
	@BeforeMethod
	public void beforeTests() {
		cleanUserData();
		((AccountStatementManagerImpl)accountStatementService).setClock(fakeClock);
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
		incomes = getByName(rootBranch.getChildren(), Account.INCOME_NAME);
		bank = getByName(rootBranch.getChildren(), Account.ASSET_NAME);
		outgoings = getByName(rootBranch.getChildren(), Account.OUTGOING_NAME);
		
		prepareSampleTransactions();
	}
	
	Account getByName(List<AccountNode> nodes, String name) {
		for (AccountNode accountNode : nodes) {
			if(accountNode.getAccount().getName().equals(name)) {
				return accountNode.getAccount();
			}
		}
		return null;
	}
	
	
	private void prepareSampleTransactions() {
		tx01Date = new GregorianCalendar(2013, Calendar.JUNE, 10);
		tx02Date = new GregorianCalendar(2013, Calendar.JUNE, 12);
		tx03Date = new GregorianCalendar(2013, Calendar.JUNE, 14);
		tx04Date = new GregorianCalendar(2013, Calendar.JUNE, 16);
		
		tx01Amount = new BigDecimal("1000");
		
		transactionManager.saveAccountTransaction(
				profile,
				incomes.getId(), 
				bank.getId(), 
				tx01Date.getTime(), tx01Amount, 
				"t1");
		
		tx02Amount = new BigDecimal("200");
		transactionManager.saveAccountTransaction(
				profile,
				bank.getId(), 
				outgoings.getId(), 
				tx02Date.getTime(), tx02Amount, 
				"t2");
		tx03Amount = new BigDecimal("150");
		transactionManager.saveAccountTransaction(
				profile,
				bank.getId(), 
				outgoings.getId(), 
				tx03Date.getTime(), tx03Amount, 
				"t3");
		tx04Amount = new BigDecimal("50");
		transactionManager.saveAccountTransaction(
				profile,
				incomes.getId(), 
				bank.getId(), 
				tx04Date.getTime(), tx04Amount, 
				"t4");
		
	}
	
	@Test
	public void shouldCalculateAccountBalanceBeforeTheGivenInterval() {
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank.getId(), tx02Date.getTime(), tx03Date.getTime());
		new AccountStatementChecker()
			.forStatement(accountStatement)
			.assertBalanceBefore(new BigDecimal("1000.0"));
	}
	
	@Test
	public void shouldCalculateAccountBalanceAfterTheGivenInterval() {
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank.getId(), tx02Date.getTime(), tx03Date.getTime());
		new AccountStatementChecker()
			.forStatement(accountStatement)
			.assertBalanceAfter(new BigDecimal("650.00"));
	}
	
	@Test
	public void shouldCalculateAccountOperationalBalanceOfTheGivenInterval() {
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank.getId(), tx02Date.getTime(), tx03Date.getTime());
		new AccountStatementChecker()
			.forStatement(accountStatement)
			.assertOperationalBalance(new BigDecimal("-350.00"));
	}
	
	@Test
	public void shouldGetEntriesInGivenInterval() {
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank.getId(), tx02Date.getTime(), tx04Date.getTime());
		new AccountStatementChecker()
			.forStatement(accountStatement)
			//							date	desc		value			src/dest
			.assertStatementIsPresent(tx02Date.getTime(), "t2", tx02Amount.negate(), outgoings);
	}
	
	@Test(dataProvider = "getAccountStatement.InvalidInputs")
	public void shouldThrowsConstraintViolationExceptionWhenInvalidInputForGetAccountStatement(
			Profile profile, Long accountId, Date begin, Date end, String expectedErrorMessage) {
		try {
			accountStatementService.getAccountStatement(profile, accountId, begin, end);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate(expectedErrorMessage);
		}
	}
	
	@DataProvider(name = "getAccountStatement.InvalidInputs")
	public Iterator<Object[]> getAccountStatementInvalidInputData() {
		beforeTests();
		List<Object[]> testData = new ArrayList<Object[]>();
		
		// no profile
		testData.add(new Object[] { null, bank.getId(), tx01Date.getTime(), tx03Date.getTime(), "{br.com.camiloporto.cloudfinance.user.LOGGED_USER_REQUIRED}" });

		// no account id informed
		testData.add(new Object[] { profile, null, tx01Date.getTime(), tx03Date.getTime(), "br.com.camiloporto.cloudfinance.report.statement.ACCOUNT_REQUIRED" });

		// begin date > end date
		testData.add(new Object[] {profile, bank.getId(), tx03Date.getTime(), tx01Date.getTime(), "br.com.camiloporto.cloudfinance.report.statement.BEGIN_DATE_GREATER_THAN_END_DATE"});
		
		return testData.iterator();
	}
	
	@Test
	public void shouldSetDefaultBeginDateIfItNotGiven() {
		
		//assuming today is 14/06/2013
		Calendar fakeToday = new GregorianCalendar(2013, Calendar.JUNE, 14);
		Mockito.when(fakeClock.today()).thenReturn(fakeToday);
				
		Date beginDate = null;
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank.getId(), beginDate, tx03Date.getTime());
		
		//expected Begin Date should be 11/06/2013 (3 days before 'today')
		new AccountStatementChecker()
			.forStatement(accountStatement)
			.assertBalanceBefore(new BigDecimal("1000.0"));
		
	}
	
	@Test
	public void shouldSetDefaultEndDateIfItNotGiven() {
		
		//assuming today is 14/06/2013
		Calendar fakeToday = new GregorianCalendar(2013, Calendar.JUNE, 14);
		Mockito.when(fakeClock.today()).thenReturn(fakeToday);
				
		Date endDate = null;
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank.getId(), tx02Date.getTime(), endDate);
		
		//expected End Date should be 'today'
		new AccountStatementChecker()
			.forStatement(accountStatement)
			.assertBalanceBefore(new BigDecimal("1000.0"));
		
	}
}
