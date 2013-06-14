package br.com.camiloporto.cloudfinance.repository;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountEntry;

@RooJpaRepository(domainType = AccountEntry.class)
public interface AccountEntryRepository {

	@Query("SELECT sum(entry.entryValue) FROM AccountEntry entry " +
			"WHERE entry.transactionDate between ?1 AND ?2 " +
			"AND entry.account = ?3")
	BigDecimal sumBetween(Date begin, Date end, Account account);
	
}
