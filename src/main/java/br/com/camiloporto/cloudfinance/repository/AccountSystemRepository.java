package br.com.camiloporto.cloudfinance.repository;

import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooJpaRepository(domainType = AccountSystem.class)
public interface AccountSystemRepository {

	List<AccountSystem> findByUserProfile(Profile profile);
	
	AccountSystem findByUserProfileAndRootAccountId(Profile profile, Long rootAccountId);
}
