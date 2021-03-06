package br.com.camiloporto.cloudfinance.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.ExceptionChecker;
import br.com.camiloporto.cloudfinance.helpers.DataInsertionHelper;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheet;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheetNode;

public class BalanceSheetManagerTest extends AbstractCloudFinanceDatabaseTest {
	
	@Autowired
	private BalanceSheetManager balanceSheetManager;

	private Profile profile;
	
	private AccountSystem accountSystem;

	private Account root;
	
	@BeforeMethod
	public void prepareData() throws IOException, ParseException {
		cleanUserData();
		final String camiloporto = "some@email.com";
		final String senha = "1234";
		Profile p = new ProfileBuilder()
			.newProfile()
			.comEmail(camiloporto)
			.comSenha(senha)
			.create();
		profile = userProfileManager.signUp(p);
		accountSystem = accountManager.findAccountSystems(profile).get(0);
		
		root = accountSystem.getRootAccount();
		accountInsertionHelper = new DataInsertionHelper(accountSystem, camiloporto, senha);
		accountInsertionHelper.insertAccountsFromFile(profile, DataInsertionHelper.ACCOUNT_DATA);
		accountInsertionHelper.insertTransactionsFromFile(profile, DataInsertionHelper.TRANSACTION_DATA);
	}
	
	@Test
	public void shouldGetBalanceSheetOnDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse("20/09/2013");
		
		BalanceSheet balanceSheet = balanceSheetManager.getBalanceSheet(profile, root.getId(), date);
		Assert.assertNotNull(balanceSheet, "balance sheet should not be null");
		Map<String, BigDecimal> expectedAccountBalances = new HashMap<String, BigDecimal>();
		expectedAccountBalances.put("Conta Corrente", new BigDecimal("700.00"));
		expectedAccountBalances.put("Cartao Credito", new BigDecimal("-25.00"));
		expectedAccountBalances.put(Account.ASSET_NAME, new BigDecimal("1200.00"));
		expectedAccountBalances.put(Account.LIABILITY_NAME, new BigDecimal("-25.00"));
		expectedAccountBalances.put("Fundo BB DI", new BigDecimal("500.00"));
		checkAccountBalances(balanceSheet, expectedAccountBalances);
	}
	
	@Test
	public void shouldNotFailIfAnAccountDoNotHaveTransactionOnGivenBalanceInterval() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//no transactions until this date. All balances should be zero
		Date date = sdf.parse("20/09/2001");
		
		BalanceSheet balanceSheet = balanceSheetManager.getBalanceSheet(profile, root.getId(), date);
		Assert.assertNotNull(balanceSheet, "balance sheet should not be null");
		Map<String, BigDecimal> expectedAccountBalances = new HashMap<String, BigDecimal>();
		expectedAccountBalances.put("Conta Corrente", new BigDecimal("0.00"));
		expectedAccountBalances.put("Cartao Credito", new BigDecimal("0.00"));
		expectedAccountBalances.put(Account.ASSET_NAME, new BigDecimal("0.00"));
		expectedAccountBalances.put(Account.LIABILITY_NAME, new BigDecimal("0.00"));
		expectedAccountBalances.put("Fundo BB DI", new BigDecimal("0.00"));
		checkAccountBalances(balanceSheet, expectedAccountBalances);
	}
	
	@Test
	public void shouldRequireDateToGetBalanceSheet() throws ParseException {
		Date date = null;
		
		try {
			balanceSheetManager.getBalanceSheet(profile, root.getId(), date);
			Assert.fail("should throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.report.balancesheet.DATE_REQUIRED}");
		}
	}
	
	@Test
	public void shouldRequireProfileToGetBalanceSheet() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse("20/09/2013");
		Profile p = null;
		try {
			balanceSheetManager.getBalanceSheet(p, root.getId(), date);
			Assert.fail("should throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.user.LOGGED_USER_REQUIRED}");
		}
	}
	
	@Test
	public void shouldRequireRootAccountToGetBalanceSheet() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse("20/09/2013");
		Long nullRootAccountId = null;
		try {
			balanceSheetManager.getBalanceSheet(profile, nullRootAccountId, date);
			Assert.fail("should throws expected exception");
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			new ExceptionChecker(e)
				.assertExpectedErrorCountIs(1)
				.assertContainsMessageTemplate("{br.com.camiloporto.cloudfinance.account.ROOT_ACCOUNT_REQUIRED}");
		}
	}
	
	private void checkAccountBalances(BalanceSheet balanceSheet,
			Map<String, BigDecimal> expectedAccountBalances) {
		checkBalanceSheetTreeValues(balanceSheet.getAssetBalanceSheetTree(), expectedAccountBalances);
		checkBalanceSheetTreeValues(balanceSheet.getLiabilityBalanceSheetTree(), expectedAccountBalances);
	}

	private void checkBalanceSheetTreeValues(
			BalanceSheetNode sheetNode,
			Map<String, BigDecimal> expectedAccountBalances) {
		Assert.assertEquals(
				sheetNode.getBalance(), 
				expectedAccountBalances.get(sheetNode.getAccount().getName()),
				"balance did not match for account " + sheetNode.getAccount().getName());
		for (BalanceSheetNode childNode : sheetNode.getChildren()) {
			checkBalanceSheetTreeValues(childNode, expectedAccountBalances);
		}
	}

	@Test
	public void shouldGetLeafAccountBalanceOnDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse("20/09/2013");
		Account anAccount = accountRepository.findByNameAndRootAccount("Conta Corrente", this.root);
		
		BigDecimal balance = balanceSheetManager.getAccountBalance(profile, anAccount, date);
		BigDecimal expectedBalance = new BigDecimal("700.00");
		Assert.assertEquals(balance, expectedBalance , "account balance did not match expected value");
	}
}
