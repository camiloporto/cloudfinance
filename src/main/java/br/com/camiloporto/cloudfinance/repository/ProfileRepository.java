package br.com.camiloporto.cloudfinance.repository;

import br.com.camiloporto.cloudfinance.model.Profile;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Profile.class)
public interface ProfileRepository {
}
