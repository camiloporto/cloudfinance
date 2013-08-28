package br.com.camiloporto.cloudfinance.web.purehtml;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.web.AbstractOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountController;
import br.com.camiloporto.cloudfinance.web.AccountOperationResponse;
import br.com.camiloporto.cloudfinance.web.AccountSystemController;
import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;
import br.com.camiloporto.cloudfinance.web.TransactionController;
import br.com.camiloporto.cloudfinance.web.TransactionOperationResponse;

@RequestMapping("/transaction")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class StaticTransactionController {
	
	@Autowired
	private TransactionController jsonController;
	
	@Autowired
	private AccountSystemController accountController;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getTransactions(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {

		TransactionOperationResponse response = jsonController.getTransactions(logged, rootAccount, begin, end);
		ModelAndView mav = new ModelAndView("mobile-transaction");
		mav.getModelMap().addAttribute("response", response);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView createTransaction(
			@ModelAttribute(value="logged") Profile logged, 
			@RequestParam("originAccountId") Long originAccountId,
			@RequestParam("destAccountId") Long destAccountId,
			@RequestParam("date") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
			@RequestParam("description") String description,
			@RequestParam("amount") @NumberFormat(style = Style.NUMBER) BigDecimal amount) {
		
		System.out.println("StaticTransactionController.createTransaction() " + amount);
		TransactionOperationResponse response = jsonController.createTransaction(logged, originAccountId, destAccountId, date, description, amount);
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
			@ModelAttribute(value="rootAccount") Account rootAccount) {
		AccountOperationResponse aor = accountController.getLeavesAccounts(logged, rootAccount.getId());
		TransactionOperationResponse tor = new TransactionOperationResponse(true);
		tor.setDestAccountList(aor.getLeafAccounts());
		tor.setOriginAccountList(aor.getLeafAccounts());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mobile-transactionNewForm");
		mav.getModelMap().addAttribute("response", tor);
		return mav;
		
	}

}
