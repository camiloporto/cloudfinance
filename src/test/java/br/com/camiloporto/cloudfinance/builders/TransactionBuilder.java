package br.com.camiloporto.cloudfinance.builders;

import java.math.BigDecimal;
import java.util.Date;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountEntry;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;

public class TransactionBuilder {
	
	private AccountTransaction transaction;

	public TransactionBuilder newTransaction() {
		transaction = new AccountTransaction();
		AccountEntry originEntry = new AccountEntry();
		AccountEntry destEntry = new AccountEntry();
		transaction.setOrigin(originEntry);
		transaction.setDestin(destEntry);
		return this;
	}

	public TransactionBuilder originAccount(String name) {
		Account a = new Account();
		a.setName(name);
		transaction.getOrigin().setAccount(a);
		return this;
	}

	public TransactionBuilder destAccount(String name) {
		Account a = new Account();
		a.setName(name);
		transaction.getDestin().setAccount(a);
		return this;
	}

	public TransactionBuilder date(Date date) {
		transaction.getOrigin().setTransactionDate(date);
		transaction.getDestin().setTransactionDate(date);
		return this;
	}

	public TransactionBuilder amount(BigDecimal amount) {
		transaction.getOrigin().setEntryValue(amount.negate());
		transaction.getDestin().setEntryValue(amount);
		return this;
	}

	public TransactionBuilder description(String description) {
		transaction.getOrigin().setComment(description);
		transaction.getDestin().setComment(description);
		return this;
	}

	public AccountTransaction getTransaction() {
		return transaction;
	}

}
