package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;

@RooJavaBean
public class AccountStatement {
	
	private BigDecimal balanceBeforeInterval;
	private BigDecimal balanceAfterInterval;
	private BigDecimal operationalBalance;
	private Account accountOfStatement;
	
	List<AccountStatementEntry> entries = new ArrayList<AccountStatementEntry>();
	List<AccountTransaction> transactions = new ArrayList<AccountTransaction>();
	
	void setTransactions(List<AccountTransaction> transactions) {
		entries.clear();
		for (AccountTransaction transaction : transactions) {
			entries.add(createAccountStatementEntry(transaction));
		}
	}

	private AccountStatementEntry createAccountStatementEntry(
			AccountTransaction transaction) {
		AccountStatementEntry e = new AccountStatementEntry();
		e.setDate(transaction.getOrigin().getTransactionDate());
		e.setDescription(transaction.getOrigin().getComment());
		Account involvedAccount = getInvolvedAccount(transaction);
		e.setInvolvedAccount(involvedAccount);
		if(isAccountOfStatementOriginOfTransaction(transaction)) {
			e.setAmount(transaction.getOrigin().getEntryValue());
		} else {
			e.setAmount(transaction.getDestin().getEntryValue());
		}
		
		return e;
	}

	private Account getInvolvedAccount(AccountTransaction transaction) {
		Account origin = transaction.getOrigin().getAccount();
		Account involved = origin;
		if(accountOfStatement.getId().equals(origin.getId())) {
			involved = transaction.getDestin().getAccount();
		}
		return involved;
	}
	
	private boolean isAccountOfStatementOriginOfTransaction(AccountTransaction transaction) {
		return transaction.getOrigin().getAccount().getId().equals(accountOfStatement.getId());
	}

}
