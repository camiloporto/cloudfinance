package br.com.camiloporto.cloudfinance.service;

import org.springframework.stereotype.Service;

import br.com.camiloporto.cloudfinance.model.Profile;

@Service
public interface UserProfileManager {
	
	public Profile signUp(Profile newProfile);

	public Profile login(String userName, String pass);
	
	public Profile findByUsername(String userName);

}
