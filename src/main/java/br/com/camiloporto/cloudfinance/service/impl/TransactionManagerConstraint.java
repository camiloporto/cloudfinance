package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Profile;

@Component
@Configurable
@RooJavaBean
public class TransactionManagerConstraint {
	
	
	public interface DELETE_TRANSACTION {}
	public interface SAVE_NEW_TRANSACTION {}
	public interface FIND_TRANSACTION_BY_DATE_INTERVAL {}
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.user.LOGGED_USER_REQUIRED",
			groups={FIND_TRANSACTION_BY_DATE_INTERVAL.class,
				DELETE_TRANSACTION.class})
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

	@NotNull(message = "br.com.camiloporto.cloudfinance.account.TREE_ROOT_ACCOUNT_REQUIRED",
			groups={FIND_TRANSACTION_BY_DATE_INTERVAL.class,
				DELETE_TRANSACTION.class})
	private Long rootAccountId;
	
	@NotNull(message = "br.com.camiloporto.cloudfinance.transaction.ID_REQUIRED",
			groups={DELETE_TRANSACTION.class})
	private Long transactionId;
	
	private Date begin;
	private Date end;
	
	@AssertTrue(message = "br.com.camiloporto.cloudfinance.transaction.BEGIN_DATE_GREATER_THAN_END_DATE", 
			groups = {FIND_TRANSACTION_BY_DATE_INTERVAL.class})
	public boolean isBeginDateLowerOrEqualsToEndDate() {
		boolean ret = true;
		if(begin != null && end != null) {
			ret = begin.compareTo(end) <= 0;
		}
		return ret;
	}
	

}
