package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountEntry;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

public class TransactionManagerImpl implements TransactionManager {
	
	@Autowired
	private AccountManager accountManager;
	
	@Override
	public AccountTransaction saveAccountTransaction(Long originAccountId, Long destAccountId,
			Date transactionDate, BigDecimal amount, String description) {
		Account origin = accountManager.findAccount(originAccountId);
		Account dest = accountManager.findAccount(destAccountId);
		
		AccountEntry originEntry = new AccountEntry();
		AccountEntry destEntry = new AccountEntry();
		
		originEntry.setAccount(origin);
		originEntry.setComment(description);
		originEntry.setEntryValue(amount.negate());
		originEntry.setTransactionDate(transactionDate);
		
		destEntry.setAccount(dest);
		destEntry.setComment(description);
		destEntry.setEntryValue(amount);
		destEntry.setTransactionDate(transactionDate);
		
		AccountTransaction transaction = new AccountTransaction();
		transaction.setOrigin(originEntry);
		transaction.setDestin(destEntry);
		
		return accountTransactionRepository.save(transaction);
	}
}
