package br.com.camiloporto.cloudfinance.web.purehtml;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;

@RequestMapping("/report")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class StaticReportController {
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET)
	public ModelAndView getAccountStatementForm(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount) {
		return new ModelAndView("mobile-statement");
	}

}
