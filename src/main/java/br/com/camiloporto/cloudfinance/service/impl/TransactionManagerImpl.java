package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountEntry;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.Clock;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

public class TransactionManagerImpl implements TransactionManager {
	
	@Autowired
	private AccountManager accountManager;
	
	private Clock clock = new Clock();
	
	public void setClock(Clock clock) {
		this.clock = clock;
	}
	
	@Override
	public AccountTransaction saveAccountTransaction(Profile profile, Long originAccountId, Long destAccountId,
			Date transactionDate, BigDecimal amount, String description) {
		
		checkSaveNewTransactionEntries(profile, originAccountId, destAccountId, transactionDate, amount, description);
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
	
	@Override
	public List<AccountTransaction> findAccountTransactionByDateBetween(
			Profile profile, Long rootAccountId, Date begin, Date end) {
		checkFindTransactionBetweenDatesEntries(profile, rootAccountId, begin, end);
		begin = getDefaultBeginIfNeeded(begin);
		end = getDefaultEndIfNeeded(end);
		return accountTransactionRepository.findByDateBetween(rootAccountId, begin,end);
	}
	
	
	private Date getDefaultEndIfNeeded(Date end) {
		Date ret = end;
		if(end == null) {
			ret = today().getTime();
		}
		return ret;
	}

	private Date getDefaultBeginIfNeeded(Date begin) {
		Date ret = begin;
		if(begin == null) {
			Calendar defaultBegin = today();
			defaultBegin.add(Calendar.DAY_OF_MONTH, -3);
			ret = defaultBegin.getTime();
		}
		return ret;
	}

	private Calendar today() {
		return clock.today();
	}

	private void checkFindTransactionBetweenDatesEntries(Profile profile,
			Long rootAccountId, Date begin, Date end) {
		TransactionManagerConstraint constraints = new TransactionManagerConstraint();
		constraints.setProfile(profile);
		constraints.setRootAccountId(rootAccountId);
		constraints.setBegin(begin);
		constraints.setEnd(end);
		
		new ConstraintValidator<TransactionManagerConstraint>()
			.validateForGroups(constraints,
				TransactionManagerConstraint.FIND_TRANSACTION_BY_DATE_INTERVAL.class);
	}

	private void checkSaveNewTransactionEntries(Profile profile, Long originAccountId, Long destAccountId,
			Date transactionDate, BigDecimal amount, String description) {
		
		TransactionManagerConstraint constraints = new TransactionManagerConstraint();
		constraints.setAmount(amount);
		constraints.setDescription(description);
		constraints.setDestAccountId(destAccountId);
		constraints.setOriginAccountId(originAccountId);
		constraints.setProfile(profile);
		constraints.setTransactionDate(transactionDate);
		
		new ConstraintValidator<TransactionManagerConstraint>()
			.validateForGroups(constraints,
				TransactionManagerConstraint.SAVE_NEW_TRANSACTION.class);
	}
}
