package br.com.camiloporto.cloudfinance.repository;

import br.com.camiloporto.cloudfinance.model.Account;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Account.class)
public interface AccountRepository {
}
