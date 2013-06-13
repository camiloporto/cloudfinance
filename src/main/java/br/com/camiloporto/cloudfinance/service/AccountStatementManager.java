package br.com.camiloporto.cloudfinance.service;

import java.util.Date;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;

public interface AccountStatementManager {

	AccountStatement getAccountStatement(Profile profile, Account account,
			Date begin, Date end);

}
