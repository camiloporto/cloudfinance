package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.AbstractCloudFinanceDatabaseTest;
import br.com.camiloporto.cloudfinance.builders.ProfileBuilder;
import br.com.camiloporto.cloudfinance.checkers.AccountStatementChecker;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;
import br.com.camiloporto.cloudfinance.service.impl.TransactionManagerImpl;

public class AccountStatementTest extends AbstractCloudFinanceDatabaseTest {
	
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
	
	private Account root;

	@Autowired
	private AccountStatementManager accountStatementService;
	
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
		incomes = getByName(rootBranch.getChildren(), Account.INCOME_NAME);
		bank = getByName(rootBranch.getChildren(), Account.ASSET_NAME);
		outgoings = getByName(rootBranch.getChildren(), Account.OUTGOING_NAME);
	}
	
	Account getByName(List<AccountNode> nodes, String name) {
		for (AccountNode accountNode : nodes) {
			if(accountNode.getAccount().getName().equals(name)) {
				return accountNode.getAccount();
			}
		}
		return null;
	}
	
	@Test
	public void shouldCalculateAccountBalanceBeforeTheIntervalInformed() {
		Calendar d1 = new GregorianCalendar(2013, Calendar.JUNE, 10);
		Calendar d2 = new GregorianCalendar(2013, Calendar.JUNE, 12);
		Calendar d3 = new GregorianCalendar(2013, Calendar.JUNE, 14);
		Calendar d4 = new GregorianCalendar(2013, Calendar.JUNE, 16);
		
		BigDecimal tx01Amount = new BigDecimal("1000");
		
		transactionManager.saveAccountTransaction(
				profile,
				incomes.getId(), 
				bank.getId(), 
				d1.getTime(), tx01Amount, 
				"t1");
		
		transactionManager.saveAccountTransaction(
				profile,
				bank.getId(), 
				outgoings.getId(), 
				d2.getTime(), new BigDecimal("200"), 
				"t2");
		transactionManager.saveAccountTransaction(
				profile,
				bank.getId(), 
				outgoings.getId(), 
				d3.getTime(), new BigDecimal("150"), 
				"t3");
		transactionManager.saveAccountTransaction(
				profile,
				incomes.getId(), 
				bank.getId(), 
				d4.getTime(), new BigDecimal("50"), 
				"t4");
		
		
		AccountStatement accountStatement = accountStatementService.getAccountStatement(profile, bank, d2.getTime(), d3.getTime());
		new AccountStatementChecker()
			.forStatement(accountStatement)
			.assertBalanceBefore(tx01Amount);
		
	}
}
