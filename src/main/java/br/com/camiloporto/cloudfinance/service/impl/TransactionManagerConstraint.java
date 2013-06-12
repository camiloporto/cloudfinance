package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Profile;

@Component
@Configurable
@RooJavaBean
public class TransactionManagerConstraint {
	
	
	public interface SAVE_NEW_TRANSACTION {}
	
	private Profile profile;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.transaction.ORIGIN_ACCOUNT_REQUIRED",
			groups={SAVE_NEW_TRANSACTION.class})
	private Long originAccountId;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.transaction.DEST_ACCOUNT_REQUIRED",
			groups={SAVE_NEW_TRANSACTION.class})
	private Long destAccountId;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.transaction.DATE_REQUIRED",
			groups={SAVE_NEW_TRANSACTION.class})
	private Date transactionDate;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.transaction.AMOUNT_REQUIRED",
			groups={SAVE_NEW_TRANSACTION.class})
	private BigDecimal amount;
	
	private String description;
	

}
