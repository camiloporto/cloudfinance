package br.com.camiloporto.cloudfinance.web.purehtml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;

@RequestMapping("/account")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class StaticAccountSystemController {
	
	@Autowired
	private AccountSystemController jsonController;
	
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

}
