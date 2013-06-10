package br.com.camiloporto.cloudfinance.web;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountEntry;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;

@RequestMapping("/transaction")
@Controller
@SessionAttributes("logged")
public class TransactionController {
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AbstractOperationResponse createTransaction(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute("newTransaction") AccountTransaction transaction) {
		System.out.println("TransactionController.createTransaction()" + transaction);
		return new TransactionOperationResponse(false);
	}
	
	@ModelAttribute("newTransaction")
	public AccountTransaction fillTransaction(
			@RequestParam("originAccountId") Long originAccountId,
			@RequestParam("destAccountId") Long destAccountId,
			@RequestParam("date") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
			@RequestParam("description") String description,
			@RequestParam("amount") @NumberFormat(style = Style.NUMBER) BigDecimal amount) {
		
		
		AccountTransaction transaction = new AccountTransaction();
		transaction.setOrigin(new AccountEntry());
		transaction.getOrigin().setAccount(new Account());
		transaction.getOrigin().getAccount().setId(originAccountId);
		transaction.getOrigin().setTransactionDate(date);
		transaction.getOrigin().setEntryValue(amount.negate());
		transaction.getOrigin().setComment(description);
		
		transaction.setDestin(new AccountEntry());
		transaction.getDestin().setAccount(new Account());
		transaction.getDestin().getAccount().setId(destAccountId);
		transaction.getDestin().setTransactionDate(date);
		transaction.getDestin().setEntryValue(amount);
		transaction.getDestin().setComment(description);
		
		
		
		return transaction;
	}
}
