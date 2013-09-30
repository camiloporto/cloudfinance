package br.com.camiloporto.cloudfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import br.com.camiloporto.cloudfinance.model.Account;

@RooJpaRepository(domainType = Account.class)
public interface AccountRepository {

	List<Account> findByParentAccount(Account parentAccount);

	Account findByNameAndRootAccount(String name, Account rootAccount);
	
	@Query("SELECT a FROM Account a WHERE a.rootAccount.id = ?1 AND a.id NOT IN (" +
			" SELECT DISTINCT a2.parentAccount.id FROM Account a2 WHERE a2.parentAccount.id IS NOT NULL" +
			")")
	List<Account> findLeavesFromRootAccountId(Long rootAccountId);

	Account findByParentAccountAndName(Account rootAccount, String accountName);
}
