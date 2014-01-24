package br.com.camiloporto.cloudfinance.repository;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import br.com.camiloporto.cloudfinance.model.Profile;

@RooJpaRepository(domainType = Profile.class)
public interface ProfileRepository {

	Profile findByUserId(String userId);

	Profile findByUserIdAndPass(String userName, String pass);
}
