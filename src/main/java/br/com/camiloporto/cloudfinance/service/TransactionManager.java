package br.com.camiloporto.cloudfinance.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.layers.service.RooService;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;

@RooService(domainTypes = { br.com.camiloporto.cloudfinance.model.AccountTransaction.class })
public interface TransactionManager {

	AccountTransaction saveAccountTransaction(Long originAccountId, Long destAccountId, 
			Date transactionDate, BigDecimal amount, String description);
}
