package br.com.camiloporto.cloudfinance.web.purehtml;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;
import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;

@RequestMapping("/account")
@Controller
@SessionAttributes(value = {"logged", "rootAccount", "activeAccountSystem"})
public class StaticAccountSystemController {
	
	@Autowired
	private AccountSystemController jsonController;
	
	@Autowired
	private AccountManager accountManager;
	
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	public ModelAndView getRootAccounts(@ModelAttribute(value="logged") Profile logged) {
		ModelAndView mav = new ModelAndView("mobile-rootAccountHome");
		AccountOperationResponse response = jsonController.getAccountSystems(logged);
		mav.getModel().put("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/{accountSystemId}", method = RequestMethod.GET)
	public ModelAndView setActiveAccountSystem(
			@ModelAttribute(value="logged") Profile logged,
			@PathVariable Long accountSystemId,
			ModelMap map) {
		AccountOperationResponse response = jsonController.setActiveAccountSystem(logged, accountSystemId, map);
		ModelAndView mav = new ModelAndView();
		if(response.isSuccess()) {
			mav.setViewName("redirect:/account");
		}
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getRootAccountBranch(
			@ModelAttribute(value="logged")  Profile logged,
			@ModelAttribute(value="activeAccountSystem")  AccountSystem activeAccountSystem) {
		ModelAndView mav =  new ModelAndView();
		mav.setViewName("redirect:/account/tree/" + activeAccountSystem.getRootAccount().getId());
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
			@ModelAttribute(value="activeAccountSystem")  AccountSystem activeAccountSystem,
			Account account) {
		ModelAndView mav = new ModelAndView();
		AccountOperationResponse response = jsonController.createAccount(logged, activeAccountSystem, account);
		if(response.isSuccess()) {
			mav.setViewName("redirect:/account/tree/" + activeAccountSystem.getRootAccount().getId());
		} else {
			mav = new ModelAndView("mobile-newAccountForm");
			//redraw account form with needed information
			if(account.getParentAccount() != null && account.getParentAccount().getId() != null) {
				Account parent = accountManager.findAccount(account.getParentAccount().getId());
				if(parent != null) {
					response.setAccount(parent);
				}
			}
		}
		mav.getModelMap().addAttribute("response", response);
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
	
	@ExceptionHandler(HttpSessionRequiredException.class)
	public ModelAndView catchRootAccountSessionAttributeRequired(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(request.getSession().getAttribute("activeAccountSystem") == null) {
			AccountOperationResponse aor = new AccountOperationResponse(false);
			aor.setErrors(new String[]{"Um sistema de contas deve ser selecionado"});
			mav.getModelMap().addAttribute("response", aor);
			mav.setViewName("redirect:/account/roots");
		}
		return mav;
	}

}
