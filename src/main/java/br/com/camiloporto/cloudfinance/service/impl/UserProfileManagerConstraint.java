package br.com.camiloporto.cloudfinance.service.impl;


import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.ProfileRepository;

@Component
@Configurable
public class UserProfileManagerConstraint {
	
	public interface SIGNUP_RULES {}

	@Autowired
	private ProfileRepository profileRepository;
	
	@Valid
	private Profile profile;
	
	public UserProfileManagerConstraint() {
	}
	
	public UserProfileManagerConstraint(Profile profile) {
		this.profile = profile;
	}
	
	@AssertTrue(groups={SIGNUP_RULES.class}, message="{br.com.camiloporto.cloudfinance.profile.USER_ID_ALREADY_EXIST}")
	public boolean isUserNameDoNotExist() {
		if(profile != null && profile.getUserId() != null) {
			return profileRepository.findByUserId(profile.getUserId()) == null;
		}
		return true;
	}

}
