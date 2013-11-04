package br.com.camiloporto.cloudfinance.security.permission;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import br.com.camiloporto.cloudfinance.service.UserProfileManager;

@Component
public class TenantPermissionEvaluator implements PermissionEvaluator {
	
	@Autowired
	private AccountSystemRepository accountSystemRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserProfileManager userProfileManager;
	
	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		String permissionStr = (String) permission;
		boolean result = false;
		if(permissionStr.startsWith(Account.class.getSimpleName() + ".")) {
			result = hasAccountPermission(authentication, targetDomainObject, permissionStr);
		} else if(permissionStr.startsWith(Profile.class.getSimpleName())) {
			result = hasProfilePermission(authentication, targetDomainObject, permissionStr);
		} else if(permissionStr.startsWith(AccountSystem.class.getSimpleName() + ".")) {
			result = hasAccountSystemPermission(authentication, targetDomainObject, permissionStr);
		}
		
		return result;
	}

	private boolean hasAccountSystemPermission(Authentication authentication,
			Object targetDomainObject, String permissionStr) {
		boolean result = false;
		AccountSystem targetAccountSystem = null;
		Long targetAccountSystemId = null;
		if(targetDomainObject instanceof Long) {
			targetAccountSystemId = (Long) targetDomainObject;
			targetAccountSystem = accountSystemRepository.findOne(targetAccountSystemId);
		}
		if(targetAccountSystem == null) {
			return true;
		}
		Profile authenticatedProfile = getAuthenticatedProfile(authentication);
		List<AccountSystem> accountSystems = accountSystemRepository.findByUserProfile(authenticatedProfile);
		result = isTargetAccountSystemInAuthenticatedProfileAccountSystemList(targetAccountSystem, accountSystems);
		return result;
	}

	private boolean isTargetAccountSystemInAuthenticatedProfileAccountSystemList(
			AccountSystem targetAccountSystem,
			List<AccountSystem> accountSystems) {
		for (AccountSystem accountSystem : accountSystems) {
			if(targetAccountSystem.getId().equals(accountSystem.getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean hasProfilePermission(Authentication authentication,
			Object targetDomainObject, String permissionStr) {
		boolean result = false;
		Profile targetProfile = null;
		if(targetDomainObject instanceof Profile) {
			targetProfile = (Profile) targetDomainObject;
		}
		Profile authenticatedProfile = getAuthenticatedProfile(authentication);
		result = authenticatedProfile.getUsername().equals(targetProfile.getUsername());
		return result;
	}

	private boolean hasAccountPermission(Authentication authentication,
			Object targetDomainObject, String permissionStr) {
		boolean result = false;
		Long accountId = null;
		Account targetAccount = null;
		if(targetDomainObject instanceof Number) {
			accountId = (Long) targetDomainObject;
			targetAccount = accountRepository.findOne(accountId);
		} else if (targetDomainObject instanceof Account) {
			targetAccount = (Account) targetDomainObject;
			accountId = targetAccount.getId();
		}
		
		if(targetAccount == null) {
			return true;
		}
		Account rootAccount = targetAccount;
		if(targetAccount.getRootAccount() != null) {
			Long rootAccountId = targetAccount.getRootAccount().getId();
			rootAccount = accountRepository.findOne(rootAccountId);
		}
		Profile profile = getAuthenticatedProfile(authentication);
		List<AccountSystem> accountSystems = accountSystemRepository.findByUserProfile(profile);
		result = isRootAccountBelongToAnyAccountSystem(rootAccount, accountSystems);
		return result;
	}

	public Profile getAuthenticatedProfile(Authentication authentication) {
		String principal = authentication.getName();
		Profile profile = userProfileManager.findByUsername(principal);
		return profile;
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
		throw new UnsupportedOperationException();
	}

}
