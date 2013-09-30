package br.com.camiloporto.cloudfinance.web.purehtml;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;
import br.com.camiloporto.cloudfinance.web.TransactionController;
import br.com.camiloporto.cloudfinance.web.TransactionForm;
import br.com.camiloporto.cloudfinance.web.TransactionOperationResponse;

@RequestMapping("/transaction")
@Controller
@SessionAttributes(value = {"logged", "activeAccountSystem"})
public class StaticTransactionController {
	
	@Autowired
	private TransactionController jsonController;
	
	@Autowired
	private AccountSystemController accountController;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getTransactions(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {

		TransactionOperationResponse response = jsonController.getTransactions(logged, activeAccountSystem, begin, end);
		ModelAndView mav = new ModelAndView("mobile-transaction");
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/{transactionId}", method = RequestMethod.GET)
	public ModelAndView showTransactionDetail(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			@PathVariable Long transactionId) {
		TransactionOperationResponse response = jsonController.getById(logged, activeAccountSystem, transactionId);
		ModelAndView mav = new ModelAndView("mobile-transactionDetail");
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			Long transactionId
			) {
		TransactionOperationResponse response = jsonController.delete(logged, activeAccountSystem, transactionId);
		ModelAndView mav = new ModelAndView();
		if(response.isSuccess()) {
			mav.setViewName("redirect:/transaction");
		} else {
			mav.setViewName("mobile-transactionNewForm");
		}
		
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView createTransaction(
			HttpServletRequest request,
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="transactionForm") TransactionForm form,
			BindingResult errors) {
		
		TransactionOperationResponse response = jsonController.createTransaction(request, logged, form, errors);
		ModelAndView mav = new ModelAndView();
		if(response.isSuccess()) {
			mav.setViewName("redirect:/transaction");
		} else {
			mav.setViewName("mobile-transactionNewForm");
		}
		
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(value = "/newForm", method = RequestMethod.GET)
	public ModelAndView showNewForm(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem) {
		AccountOperationResponse aor = accountController.getLeavesAccounts(logged, activeAccountSystem.getRootAccount().getId());
		TransactionOperationResponse tor = new TransactionOperationResponse(true);
		tor.setDestAccountList(aor.getLeafAccounts());
		tor.setOriginAccountList(aor.getLeafAccounts());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mobile-transactionNewForm");
		mav.getModelMap().addAttribute("response", tor);
		return mav;
		
	}

}
