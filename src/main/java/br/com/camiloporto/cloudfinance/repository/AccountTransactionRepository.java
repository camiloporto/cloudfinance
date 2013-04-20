package br.com.camiloporto.cloudfinance.repository;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = AccountTransaction.class)
public interface AccountTransactionRepository {
}
