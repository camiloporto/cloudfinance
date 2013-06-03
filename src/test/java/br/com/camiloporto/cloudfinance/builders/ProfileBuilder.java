package br.com.camiloporto.cloudfinance.builders;

import br.com.camiloporto.cloudfinance.model.Profile;

public class ProfileBuilder {
	
	private Profile profile;

	public ProfileBuilder newProfile() {
		profile = new Profile();
		return this;
	}

	public ProfileBuilder comEmail(String email) {
		profile.setUserId(email);
		return this;
	}

	public ProfileBuilder comSenha(String pass) {
		profile.setPass(pass);
		return this;
	}

	public Profile create() {
		return profile;
	}

}
