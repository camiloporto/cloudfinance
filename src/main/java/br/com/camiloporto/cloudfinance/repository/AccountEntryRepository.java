package br.com.camiloporto.cloudfinance.repository;

import br.com.camiloporto.cloudfinance.model.AccountEntry;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = AccountEntry.class)
public interface AccountEntryRepository {
}
