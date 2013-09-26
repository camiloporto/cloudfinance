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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountStatementManager;
import br.com.camiloporto.cloudfinance.service.BalanceSheetManager;
import br.com.camiloporto.cloudfinance.service.Clock;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;
import br.com.camiloporto.cloudfinance.service.impl.BalanceSheet;

@RequestMapping("/report")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class ReportController {
	
	@Autowired
	private AccountStatementManager accountStatementManager;
	
	@Autowired
	private BalanceSheetManager balanceSheetManager;
	
	@Autowired
	private MessageSource messageSource;
	
	private Clock clock = new Clock();
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE, params={"accountId", "begin", "end"})
	public @ResponseBody ReportOperationResponse getAccountStatement(
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount,
			Long accountId,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date begin,
			@DateTimeFormat(pattern="dd/MM/yyyy") Date end) {

		ReportOperationResponse response = new ReportOperationResponse(false);
		try {
			AccountStatement statement = accountStatementManager.getAccountStatement(logged, accountId, begin, end);
			response.setSuccess(true);
			response.setAccountStatement(statement);
		} catch (ConstraintViolationException e) {
			response = new ReportOperationResponse(e);
		}
		
		return response;
	}
	
	@RequestMapping(value = "/balancesheet", method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ReportOperationResponse getBalanceSheet(
			HttpServletRequest request,
			@ModelAttribute(value="logged") Profile logged, 
			@ModelAttribute(value="rootAccount") Account rootAccount,
			@ModelAttribute(value = "balanceSheetForm") BalanceSheetForm form, BindingResult errors) {

		ReportOperationResponse response = new ReportOperationResponse(false);
		
		if(errors.hasErrors()) {
			String[] errorMessages = getBindingErrorMessages(errors, request.getLocale()).toArray(new String[]{});
			response.setErrors(errorMessages);
		} else {
			Date date = getDefaultDateIfNeeded(form);
			
			try {
				BalanceSheet balanceSheet = balanceSheetManager.getBalanceSheet(logged, rootAccount.getId(), date);
				response.setSuccess(true);
				response.setBalanceSheet(balanceSheet);
			} catch (ConstraintViolationException e) {
				response = new ReportOperationResponse(e);
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

	private Date getDefaultDateIfNeeded(BalanceSheetForm form) {
		if(form.getBalanceDate() == null) {
			return clock.today().getTime();
		}
		return form.getBalanceDate();
	}

}
