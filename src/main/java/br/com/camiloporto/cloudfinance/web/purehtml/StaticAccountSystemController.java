package br.com.camiloporto.cloudfinance.web.purehtml;

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
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;

@RequestMapping("/account")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class StaticAccountSystemController {
	
	@Autowired
	private AccountSystemController jsonController;
	
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	public ModelAndView getRootAccounts(@ModelAttribute(value="logged") Profile logged) {
		ModelAndView mav = new ModelAndView("mobile-rootAccountHome");
		AccountOperationResponse response = jsonController.getRootAccounts(logged);
		mav.getModel().put("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/tree/{accountId}", method = RequestMethod.GET)
	public ModelAndView getAccountBranch(
			@ModelAttribute(value="logged") Profile logged,
			@PathVariable Long accountId) {

		AccountOperationResponse response = jsonController.getAccountBranch(logged, accountId);
		ModelAndView mav = new ModelAndView("mobile-accountHome");
		mav.getModelMap().put("response", response);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView createAccount(
			@ModelAttribute(value="logged")  Profile logged,
			@ModelAttribute(value="rootAccount")  Account rootAccount,
			Account account) {
		ModelAndView mav = new ModelAndView();
		AccountOperationResponse response = jsonController.createAccount(logged, rootAccount, account);
		if(response.isSuccess()) {
			mav.setViewName("redirect:/account/tree/" + rootAccount.getId());
			mav.getModelMap().addAttribute("response", response);
		}
		return mav;
	}
	
	@RequestMapping(value = "/showForm/{parentId}", method = RequestMethod.GET)
	public ModelAndView showFormAddAccount(@ModelAttribute(value="logged") Profile logged, @PathVariable Long parentId) {
		Account parent = accountManager.findAccount(parentId);
		ModelAndView mav = null;
		if(parent != null) {
			mav = new ModelAndView("mobile-newAccountForm");
			AccountOperationResponse aor = new AccountOperationResponse(true);
			aor.setAccount(parent);
			mav.getModelMap().addAttribute("response", aor);
		}
		return mav;
	}

}
