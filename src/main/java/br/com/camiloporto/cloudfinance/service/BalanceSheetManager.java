package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.security.access.prepost.PreAuthorize;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheet;

public interface BalanceSheetManager {

	@PreAuthorize("hasPermission(#rootAccountId, 'Account.read')")
	BalanceSheet getBalanceSheet(Profile profile, Long rootAccountId, Date date);

	@Deprecated
	// see AccountStatementManager.getAccountBalanceOn() 
	BigDecimal getAccountBalance(Profile profile, Account account, Date date);

}
