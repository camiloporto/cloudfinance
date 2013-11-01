package br.com.camiloporto.cloudfinance.security.permission;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@Component
public class TenantPermissionEvaluator implements PermissionEvaluator {
	
//	@Autowired
	private AccountManager accountManager;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@PostConstruct
	public void init() {
		//FIXME improve this dependency inject. Autowire is causing cirular dependency
		accountManager = (AccountManager) applicationContext.getBean("accountManagerImpl");
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		String permissionStr = (String) permission;
		boolean result = false;
		if(permissionStr.startsWith(Account.class.getSimpleName())) {
			result = hasAccountPermission(authentication, targetDomainObject, permissionStr);
		}
		return result;
	}

	private boolean hasAccountPermission(Authentication authentication,
			Object targetDomainObject, String permissionStr) {
		boolean result = false;
		if(targetDomainObject instanceof Number) {
			Long accountId = (Long) targetDomainObject;
			Account targetAccount = accountManager.findAccount(accountId);
			if(targetAccount == null) {
				return true;
			}
			Account rootAccount = targetAccount;
			if(targetAccount.getRootAccount() != null) {
				Long rootAccountId = targetAccount.getRootAccount().getId();
				rootAccount = accountManager.findAccount(rootAccountId);
			}
			String principal = authentication.getName();
			Profile profile = userProfileManager.findByUsername(principal);
			List<AccountSystem> accountSystems = accountManager.findAccountSystems(profile);
			result = isRootAccountBelongToAnyAccountSystem(rootAccount, accountSystems);
		}
		return result;
	}

	private boolean isRootAccountBelongToAnyAccountSystem(
			Account rootAccount, List<AccountSystem> accountSystems) {
		for (AccountSystem accountSystem : accountSystems) {
			if(accountSystem.getRootAccount().getId().equals(rootAccount.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		System.out.println("TenantPermissionEvaluator.hasPermission() " + targetId + " " + targetType);
		return true;
	}

}
