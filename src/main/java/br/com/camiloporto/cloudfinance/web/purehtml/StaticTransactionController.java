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
import br.com.camiloporto.cloudfinance.web.MediaTypeApplicationJsonUTF8;
import br.com.camiloporto.cloudfinance.web.TransactionController;
import br.com.camiloporto.cloudfinance.web.TransactionOperationResponse;

@RequestMapping("/transaction")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class StaticTransactionController {
	
	@Autowired
	private TransactionController jsonController;
	
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

}
