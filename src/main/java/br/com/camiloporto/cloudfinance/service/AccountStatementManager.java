package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.security.access.prepost.PreAuthorize;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;

public interface AccountStatementManager {

	@PreAuthorize("hasPermission(#accountId, 'Account.read')")
	AccountStatement getAccountStatement(Profile profile, Long accountId,
			Date begin, Date end);
	
	@PreAuthorize("hasPermission(#a, 'Account.read')")
	public BigDecimal getAccountBalanceOn(Account a, Date date);

}
