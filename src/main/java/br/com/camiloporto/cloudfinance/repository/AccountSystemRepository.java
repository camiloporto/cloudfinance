package br.com.camiloporto.cloudfinance.repository;

import java.util.List;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = AccountSystem.class)
public interface AccountSystemRepository {

	List<AccountSystem> findByUserProfile(Profile profile);
}
