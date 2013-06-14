package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountEntryRepository;
import br.com.camiloporto.cloudfinance.repository.AccountTransactionRepository;
import br.com.camiloporto.cloudfinance.service.AccountStatementManager;

@Service
public class AccountStatementManagerImpl implements AccountStatementManager {
	
	@Autowired
	private AccountEntryRepository accountEntryRepository;
	
	@Autowired
	private AccountTransactionRepository accountTransactionRepository;
	
	private final Date LOWEST_DATE;
	private final Date HIGHEST_DATE;
	
	public AccountStatementManagerImpl() {
		LOWEST_DATE = lowestDate();
		HIGHEST_DATE = highestDate();
	}

	private Date highestDate() {
		Calendar max = Calendar.getInstance();
		max.set(Calendar.YEAR, max.getActualMaximum(Calendar.YEAR));
		max.set(Calendar.MONTH, max.getActualMaximum(Calendar.MONTH));
		max.set(Calendar.DAY_OF_MONTH, max.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return max.getTime();
	}

	private Date lowestDate() {
		Calendar min = Calendar.getInstance();
		min.set(Calendar.YEAR, min.getActualMinimum(Calendar.YEAR));
		min.set(Calendar.MONTH, min.getActualMinimum(Calendar.MONTH));
		min.set(Calendar.DAY_OF_MONTH, min.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return min.getTime();
	}
	
	private Date getBefore(Date date) {
		Calendar cal = createFromDate(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	private Calendar createFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	@Override
	public AccountStatement getAccountStatement(Profile profile,
			Account account, Date begin, Date end) {
		AccountStatement as = new AccountStatement();
		BigDecimal balanceBefore = accountEntryRepository.sumBetween(LOWEST_DATE, getBefore(begin), account);
		BigDecimal operationalBalance = accountEntryRepository.sumBetween(begin, end, account);
		BigDecimal balanceAfter = balanceBefore.add(operationalBalance);
		as.setBalanceBeforeInterval(balanceBefore);
		as.setOperationalBalance(operationalBalance);
		as.setBalanceAfterInterval(balanceAfter);
		as.setAccountOfStatement(account);
		
		List<AccountTransaction> entries = accountTransactionRepository.findByAccountAndDateBetween(account, begin, end);
		as.setTransactions(entries);
		return as;
	}

}
