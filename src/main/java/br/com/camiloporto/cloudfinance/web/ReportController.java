package br.com.camiloporto.cloudfinance.web;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.AccountStatementManager;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;

@RequestMapping("/report")
@Controller
@SessionAttributes(value = {"logged", "rootAccount"})
public class ReportController {
	
	@Autowired
	private AccountStatementManager accountStatementManager;
	
	@RequestMapping(value = "/statement", method = RequestMethod.GET, produces = MediaTypeApplicationJsonUTF8.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody AbstractOperationResponse getAccountStatement(
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

}
