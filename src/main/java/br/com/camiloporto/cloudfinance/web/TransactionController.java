package br.com.camiloporto.cloudfinance.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

@RequestMapping("/transaction")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class TransactionController {
	
	@Autowired
	private TransactionManager transactionManager;
	
	//TODO refatorar isso. tirar anotacoes desnecessarias "@RequestParam"
	@RequestMapping(method = RequestMethod.POST, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse createTransaction(
			@ModelAttribute(value="logged") Profile logged, 
			@RequestParam("originAccountId") Long originAccountId,
			@RequestParam("destAccountId") Long destAccountId,
			@RequestParam("date") @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
			@RequestParam("description") String description,
			@RequestParam("amount") @NumberFormat(style = Style.NUMBER) BigDecimal amount) {
		
		TransactionOperationResponse response = new TransactionOperationResponse(false);
		try {
			AccountTransaction transaction = 
					transactionManager.saveAccountTransaction(logged, originAccountId, destAccountId, date, amount, description);
			response = new TransactionOperationResponse(true, transaction);
		} catch (ConstraintViolationException e) {
			response = new TransactionOperationResponse(e);
		}
		
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse getTransactions(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {

		TransactionOperationResponse response = new TransactionOperationResponse(false);
		try {
			List<AccountTransaction> result = 
					transactionManager.findAccountTransactionByDateBetween(logged, rootAccount.getId(), begin, end);
			response = new TransactionOperationResponse(true, result);
		} catch(ConstraintViolationException e) {
			response = new TransactionOperationResponse(e);
		}
		
		return response;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse delete(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount,
			Long id
			) {
		TransactionOperationResponse response = new TransactionOperationResponse(false);
		
		try {
			transactionManager.deleteAccountTransaction(logged, rootAccount.getId(), id);
			response.setSuccess(true);
		} catch(ConstraintViolationException e) {
			response = new TransactionOperationResponse(e);
		}
		return response;
	}
	
}
