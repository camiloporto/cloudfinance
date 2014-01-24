package br.com.camiloporto.cloudfinance.web;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class TransactionForm {
	
	private Long originAccountId;
	private Long destAccountId;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date date;
	
	@NumberFormat(style = Style.NUMBER)
	private BigDecimal amount;
	private String description;

}
