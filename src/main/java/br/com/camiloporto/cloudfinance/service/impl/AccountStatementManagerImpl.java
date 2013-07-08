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
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.repository.AccountTransactionRepository;
import br.com.camiloporto.cloudfinance.service.AccountStatementManager;
import br.com.camiloporto.cloudfinance.service.Clock;

@Service
public class AccountStatementManagerImpl implements AccountStatementManager {
	
	@Autowired
	private AccountEntryRepository accountEntryRepository;
	
	@Autowired
	private AccountTransactionRepository accountTransactionRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private Clock clock = new Clock();
	
	private final Date LOWEST_DATE;
	private final Date HIGHEST_DATE;
	
	public void setClock(Clock clock) {
		this.clock = clock;
	}
	
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
			Long accountId, Date begin, Date end) {
		checkGetAccountStatementEntries(profile, accountId, begin, end);
		AccountStatement as = new AccountStatement();
		Account account = accountRepository.findOne(accountId);
		
		begin = getDefaultBeginIfNeeded(begin);
		end = getDefaultEndIfNeeded(end);
		
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

	private void checkGetAccountStatementEntries(Profile profile,
			Long accountId, Date begin, Date end) {
		
		AccountStatementConstraint constraints = new AccountStatementConstraint();
		constraints.setProfile(profile);
		constraints.setAccountId(accountId);
		constraints.setBegin(begin);
		constraints.setEnd(end);
		
		new ConstraintValidator<AccountStatementConstraint>()
			.validateForGroups(constraints,
				AccountStatementConstraint.ACCOUNT_STATEMENT.class);
		
	}

}
