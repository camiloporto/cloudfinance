package br.com.camiloporto.cloudfinance.web.purehtml;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.web.AccountController;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;
import br.com.camiloporto.cloudfinance.web.ReportController;
import br.com.camiloporto.cloudfinance.web.ReportOperationResponse;

@RequestMapping("/report")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class StaticReportController {
	
	@Autowired
	private ReportController jsonController;
	
	@Autowired
	private AccountSystemController accountController;
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET)
	public ModelAndView getAccountStatementForm(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount) {
		AccountOperationResponse aor = accountController.getLeavesAccounts(logged, rootAccount.getId());
		ReportOperationResponse ror = new ReportOperationResponse(true);
		ror.setAccountList(aor.getLeafAccounts());
		ModelAndView mav = new ModelAndView("mobile-statement");
		mav.getModelMap().addAttribute("response", ror);
		return mav;
	}
	
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET, params={"accountId", "begin", "end"})
	public ModelAndView getAccountStatement(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount,
			Long accountId,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {
		
		ModelAndView mav = new ModelAndView("mobile-statement");
		AccountOperationResponse aor = accountController.getLeavesAccounts(logged, rootAccount.getId());
		ReportOperationResponse ror = jsonController.getAccountStatement(logged, rootAccount, accountId, begin, end);
		ror.setAccountList(aor.getLeafAccounts());
		mav.getModelMap().addAttribute("response", ror);
		return mav;
	}

}
