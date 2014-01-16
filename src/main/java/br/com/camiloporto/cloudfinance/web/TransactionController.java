package br.com.camiloporto.cloudfinance.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.AccountTransaction;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.TransactionManager;

@RequestMapping("/transaction")
@Controller
@SessionAttributes(value = {"logged", "activeAccountSystem"})
public class TransactionController {
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse createTransaction(
			HttpServletRequest request,
			@ModelAttribute(value="logged") Profile logged,
			@ModelAttribute(value="transactionForm") TransactionForm form,
			BindingResult errors) {
		
		TransactionOperationResponse response = new TransactionOperationResponse(false);
		if(errors.hasErrors()) {
			String[] errorMessages = getBindingErrorMessages(errors, request.getLocale()).toArray(new String[]{});
			response.setErrors(errorMessages);
		} else {
			try {
				AccountTransaction transaction = 
						transactionManager.saveAccountTransaction(
								logged, 
								form.getOriginAccountId(), 
								form.getDestAccountId(), 
								form.getDate(), 
								form.getAmount(), 
								form.getDescription());
				response = new TransactionOperationResponse(true, transaction);
			} catch (ConstraintViolationException e) {
				e.printStackTrace();
				response = new TransactionOperationResponse(e);
			}
		}
		return response;
	}
	
	private List<String> getBindingErrorMessages(BindingResult errors, Locale locale) {
		List<String> result = new ArrayList<String>();
		for (ObjectError error : errors.getAllErrors()) {
			result.add(messageSource.getMessage(error, locale));
		}
		return result;
	}
	
	@RequestMapping(value = "/{transactionId}", method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse getById(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			@PathVariable Long transactionId) {
		TransactionOperationResponse response = new TransactionOperationResponse(false);
		try {
			AccountTransaction t = transactionManager.findAccountTransaction(transactionId);
			if(t != null) {
				response = new TransactionOperationResponse(true);
				response.setTransaction(t);
			}
		} catch (ConstraintViolationException e) {
			response = new TransactionOperationResponse(e);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse getTransactions(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {
		
		TransactionOperationResponse response = new TransactionOperationResponse(false);
		try {
			List<AccountTransaction> result = 
					transactionManager.findAccountTransactionByDateBetween(logged, activeAccountSystem.getRootAccount().getId(), begin, end);
			response = new TransactionOperationResponse(true, result);
			response.setBeginDateFilter(begin);
			response.setEndDateFilter(end);
		} catch(ConstraintViolationException e) {
			response = new TransactionOperationResponse(e);
		}
		return response;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody TransactionOperationResponse delete(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="activeAccountSystem") AccountSystem activeAccountSystem,
			Long id
			) {
		TransactionOperationResponse response = new TransactionOperationResponse(false);
		//FIXME verificar por que na remocao de uma transacao, a rootAccount eh alterada
		try {
			transactionManager.deleteAccountTransaction(logged, activeAccountSystem.getRootAccount().getId(), id);
			response.setSuccess(true);
		} catch(ConstraintViolationException e) {
			response = new TransactionOperationResponse(e);
		}
		return response;
	}
	
}
