package br.com.camiloporto.cloudfinance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.ProfileRepository;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@Service
public class UserProfileManagerImpl implements UserProfileManager {
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private AccountManager accountManager;
	
	@Override
	public Profile signUp(Profile newProfile) {
		checkSignUpConstraints(newProfile);
		Profile saved = profileRepository.save(newProfile);
		accountManager.createAccountSystemFor(saved);
		return saved;
	}
	
	@Transactional
	public Profile login(String userName, String pass) {
		Profile profile = profileRepository.findByUserIdAndPass(userName, pass);
		return profile;
	}
	
	private void checkSignUpConstraints(Profile profile) {
		new ConstraintValidator<UserProfileManagerConstraint>()
			.validateForGroups(
					new UserProfileManagerConstraint(profile),
					UserProfileManagerConstraint.SIGNUP_RULES.class);
	}
}
