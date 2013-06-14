package br.com.camiloporto.cloudfinance.checkers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatementEntry;

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

	public AccountStatementChecker assertBalanceAfter(BigDecimal expectedBalanceAfter) {
		Assert.assertTrue(accountStatement.getBalanceAfterInterval().compareTo(expectedBalanceAfter) == 0, "balance after not equals");
		return this;
	}

	public AccountStatementChecker assertOperationalBalance(BigDecimal expectedOperationalBalance) {
		Assert.assertTrue(accountStatement.getOperationalBalance().compareTo(expectedOperationalBalance) == 0, "operational balance not equals");
		return this;
	}

	public AccountStatementChecker assertStatementIsPresent(Date date, String desc,
			BigDecimal amount, Account srcOrDest) {
		List<AccountStatementEntry> entries = accountStatement.getEntries();
		boolean found = false;
		for (AccountStatementEntry entry : entries) {
			found = new DateChecker().isDayMonthYearEquals(date, entry.getDate()) &&
					desc.equalsIgnoreCase(entry.getDescription()) &&
					amount.compareTo(entry.getAmount()) == 0 &&
					srcOrDest.getId().equals(entry.getInvolvedAccount().getId());
			if(found) break;
		}
		Assert.assertTrue(found, "expected AccountStatementEntry not found: [" + date + ", " + desc + ", " + amount + " , " + srcOrDest.getName());
		return this;
	}

}
