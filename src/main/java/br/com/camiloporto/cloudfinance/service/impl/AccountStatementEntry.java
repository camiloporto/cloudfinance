package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;

import br.com.camiloporto.cloudfinance.model.Account;

@RooJavaBean
public class AccountStatementEntry {
	
	private Date date;
	private String description;
	private BigDecimal amount;
	private Account involvedAccount;

}
