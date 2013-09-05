package br.com.camiloporto.cloudfinance.web;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;

@RequestMapping("/account")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class AccountSystemController {
	
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value = "/roots", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AccountOperationResponse getRootAccounts(@ModelAttribute(value="logged") Profile logged) {
		List<Account> rootAccounts = accountManager.findRootAccounts(logged);
		Account[] result = new Account[rootAccounts.size()];
		rootAccounts.toArray(result);
		AccountOperationResponse response = new AccountOperationResponse(true);
		response.setRootAccounts(result);
		
		return response;
	}
	
	@RequestMapping(value = "/leaf/{accountId}", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AccountOperationResponse getLeavesAccounts(
			@ModelAttribute(value="logged") Profile logged,
			@PathVariable Long accountId) {
		List<Account> leavesAccounts = accountManager.findAllLeavesFrom(logged, accountId);
		Account[] result = new Account[leavesAccounts.size()];
		leavesAccounts.toArray(result);
		AccountOperationResponse response = new AccountOperationResponse(true);
		response.setLeafAccounts(result);
		
		return response;
	}
	
	@RequestMapping(value = "/tree/{accountId}", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AccountOperationResponse getAccountBranch(
			@ModelAttribute(value="logged") Profile logged,
			@PathVariable Long accountId) {
		AccountNode rootNode = accountManager.getAccountBranch(logged, accountId);
		AccountOperationResponse response = new AccountOperationResponse(true);
		response.setAccountTree(rootNode);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AccountOperationResponse createAccount(
			@ModelAttribute(value="logged")  Profile logged,
			@ModelAttribute(value="rootAccount")  Account rootAccount,
			Account account) {
		AccountOperationResponse response = new AccountOperationResponse(false);
		try {
			account.setRootAccount(rootAccount);
			accountManager.saveAccount(logged, account);
			response.setSuccess(true);
			response.setAccount(account);
		} catch (ConstraintViolationException e) {
			response = new AccountOperationResponse(e);
		}
		return response;
	}

	@RequestMapping(value = "/{rootAccountId}", method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody AccountOperationResponse setActiveRootAccount(
			@ModelAttribute(value="logged") Profile logged,
			@PathVariable Long rootAccountId, 
			ModelMap map) {
		Account a = accountManager.findAccount(rootAccountId);
		AccountOperationResponse response = new AccountOperationResponse(false);
		if(a != null && isRoot(a)) {
			map.addAttribute("rootAccount", a);
			response.setAccount(a);
			response = new AccountOperationResponse(true);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody AccountOperationResponse getRootAccountBranch(
			@ModelAttribute(value="logged")  Profile logged,
			@ModelAttribute(value="rootAccount")  Account rootAccount) {
		return getAccountBranch(logged, rootAccount.getId());
	}

	private boolean isRoot(Account a) {
		return a.getParentAccount() == null;
	}

}
