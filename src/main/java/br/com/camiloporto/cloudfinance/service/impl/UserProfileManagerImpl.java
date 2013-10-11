package br.com.camiloporto.cloudfinance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.model.UserRole;
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
	@Transactional
	public Profile signUp(Profile newProfile) {
		checkSignUpConstraints(newProfile);
		UserRole role = new UserRole();
		role.setAuthority("ROLE_USER");
		newProfile.getAuthorities().add(role);
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

	@Override
	public Profile findByUsername(String userName) {
		Profile profile = profileRepository.findByUserId(userName);
		return profile;
	}
}
