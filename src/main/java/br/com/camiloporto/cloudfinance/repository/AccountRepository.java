package br.com.camiloporto.cloudfinance.repository;

import java.util.List;

import br.com.camiloporto.cloudfinance.model.Account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Account.class)
public interface AccountRepository {

	List<Account> findByParentAccount(Account parentAccount);

	Account findByName(String name);
	
	@Query("SELECT a FROM Account a WHERE a.rootAccount.id = ?1 AND a.id NOT IN (" +
			" SELECT DISTINCT a2.parentAccount.id FROM Account a2 WHERE a2.parentAccount.id IS NOT NULL" +
			")")
	List<Account> findLeavesFrom(Long id);
}
