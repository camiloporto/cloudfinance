package br.com.camiloporto.cloudfinance.checkers;

import java.math.BigDecimal;

import org.testng.Assert;

import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;

public class AccountStatementChecker {
	
	private AccountStatement accountStatement;

	public AccountStatementChecker() {
	}

	public AccountStatementChecker forStatement(AccountStatement accountStatement) {
		this.accountStatement = accountStatement;
		return this;
	}

	public AccountStatementChecker assertBalanceBefore(BigDecimal expectedBeforeBalance) {
		Assert.assertTrue(accountStatement.getBalanceBeforeInterval().compareTo(expectedBeforeBalance) == 0, "balance before not equals");
		return this;
	}

}
