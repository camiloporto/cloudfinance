package br.com.camiloporto.cloudfinance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.ProfileRepository;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@Service
public class UserProfileManagerImpl implements UserProfileManager {
	
	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public Profile signUp(Profile newProfile) {
		Profile saved = profileRepository.save(newProfile);
		return saved;
	}
}
