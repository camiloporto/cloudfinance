package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;

public interface AccountStatementManager {

	AccountStatement getAccountStatement(Profile profile, Long accountId,
			Date begin, Date end);
	
	public BigDecimal getAccountBalanceOn(Account a, Date date);

}
