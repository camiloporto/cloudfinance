package br.com.camiloporto.cloudfinance.repository;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = AccountSystem.class)
public interface AccountSystemRepository {
}
