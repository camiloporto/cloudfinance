package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooService(domainTypes = { br.com.camiloporto.cloudfinance.model.AccountTransaction.class })
public interface TransactionManager {

	AccountTransaction saveAccountTransaction(Profile profile, Long originAccountId, Long destAccountId, 
			Date transactionDate, BigDecimal amount, String description);

	List<AccountTransaction> findAccountTransactionByDateBetween(
			Profile profile, Long rootAccountId, Date begin, Date end);

	void deleteAccountTransaction(Profile profile, Long treeRootAccountId, Long txId);
}
