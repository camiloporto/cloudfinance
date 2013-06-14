package br.com.camiloporto.cloudfinance.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;

@RooJpaRepository(domainType = AccountTransaction.class)
public interface AccountTransactionRepository {
	
	@Query("SELECT t FROM AccountTransaction t WHERE " +
			"t.origin.account.rootAccount.id = ?1 AND " +
			"t.destin.account.rootAccount.id = ?1 AND " +
			"t.origin.transactionDate between ?2 AND ?3")
	List<AccountTransaction> findByDateBetween(Long rootAccountId, Date begin, Date end);

	@Query("SELECT t FROM AccountTransaction t WHERE " +
			"(t.origin.account = ?1 OR t.destin.account = ?1) AND " +
			"t.origin.transactionDate between ?2 AND ?3 " +
			"ORDER BY t.origin.transactionDate ASC")
	List<AccountTransaction> findByAccountAndDateBetween(Account account,
			Date begin, Date end);
}
