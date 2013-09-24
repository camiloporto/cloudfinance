package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheet;

public interface BalanceSheetManager {

	BalanceSheet getBalanceSheet(Profile profile, Long rootAccountId, Date date);

	BigDecimal getAccountBalance(Profile profile, Account account, Date date);

}
