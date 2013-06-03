package br.com.camiloporto.cloudfinance.service;

import org.springframework.roo.addon.layers.service.RooService;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;

@RooService(domainTypes = { br.com.camiloporto.cloudfinance.model.Account.class })
public interface AccountManager {
	
	AccountSystem createAccountSystemFor(Profile p);
}
