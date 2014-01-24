package br.com.camiloporto.cloudfinance.web;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class BalanceSheetForm {
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date balanceDate;

}
