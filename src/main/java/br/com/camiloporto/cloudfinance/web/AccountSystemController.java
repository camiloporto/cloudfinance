package br.com.camiloporto.cloudfinance.web;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
@SessionAttributes("logged")
public class AccountSystemController {
	
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value = "/roots", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse getRootAccounts(@ModelAttribute(value="logged") Profile logged) {
		List<Account> rootAccounts = accountManager.findRootAccounts(logged);
		Account[] result = new Account[rootAccounts.size()];
		rootAccounts.toArray(result);
		AccountOperationResponse response = new AccountOperationResponse(true);
		response.setRootAccounts(result);
		
		return response;
	}
	
	@RequestMapping(value = "/tree/{accountId}", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse getAccountBranch(
			@ModelAttribute(value="logged") Profile logged,
			@PathVariable Long accountId) {

		AccountNode rootNode = accountManager.getAccountBranch(logged, accountId);
		AccountOperationResponse response = new AccountOperationResponse(true);
		response.setAccountTree(rootNode);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse createAccount(
			@ModelAttribute(value="logged")  Profile logged,
			Account account) {
		AccountOperationResponse response = new AccountOperationResponse(true);
		try {
			accountManager.saveAccount(logged, account);
			response.setAccount(account);
		} catch (ConstraintViolationException e) {
			response = new AccountOperationResponse(e);
		}
		return response;
	}

}
