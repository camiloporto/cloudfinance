package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;
import org.springframework.security.access.prepost.PreAuthorize;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooService(domainTypes = { br.com.camiloporto.cloudfinance.model.AccountTransaction.class })
public interface TransactionManager {

	@PreAuthorize("hasPermission(#originAccountId, 'Account.read') and hasPermission(#destAccountId, 'Account.read')")
	AccountTransaction saveAccountTransaction(Profile profile, Long originAccountId, Long destAccountId, 
			Date transactionDate, BigDecimal amount, String description);

	@PreAuthorize("hasPermission(#rootAccountId, 'Account.read')")
	List<AccountTransaction> findAccountTransactionByDateBetween(
			Profile profile, Long rootAccountId, Date begin, Date end);

	@PreAuthorize("hasPermission(#txId, 'AccountTransaction.read')")
	void deleteAccountTransaction(Profile profile, Long treeRootAccountId, Long txId);
}
