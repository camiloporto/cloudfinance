package br.com.camiloporto.cloudfinance.web;

import javax.validation.ConstraintViolationException;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.service.impl.AccountStatement;

@RooJavaBean
public class ReportOperationResponse extends AbstractOperationResponse {
	
	private AccountStatement accountStatement;
	
	private Account[] accountList;

	public ReportOperationResponse(boolean success) {
		super(success);
	}

	public ReportOperationResponse(ConstraintViolationException e) {
		super(e);
	}
}
