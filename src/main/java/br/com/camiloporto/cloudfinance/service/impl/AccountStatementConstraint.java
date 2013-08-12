package br.com.camiloporto.cloudfinance.service.impl;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.stereotype.Component;

import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.service.impl.TransactionManagerConstraint.FIND_TRANSACTION_BY_DATE_INTERVAL;

@Component
@Configurable
@RooJavaBean
public class AccountStatementConstraint {
	
	public interface ACCOUNT_STATEMENT {}

	@NotNull(message = "{br.com.camiloporto.cloudfinance.user.LOGGED_USER_REQUIRED}",
			groups={ACCOUNT_STATEMENT.class})
	private Profile profile;
	
	@NotNull(message = "{br.com.camiloporto.cloudfinance.report.statement.ACCOUNT_REQUIRED}",
			groups = {ACCOUNT_STATEMENT.class})
	private Long accountId;
	
	private Date begin;
	private Date end;
	
	@AssertTrue(message = "{br.com.camiloporto.cloudfinance.report.statement.BEGIN_DATE_GREATER_THAN_END_DATE}", 
			groups = {ACCOUNT_STATEMENT.class})
	public boolean isBeginDateLowerOrEqualsToEndDate() {
		boolean ret = true;
		if(begin != null && end != null) {
			ret = begin.compareTo(end) <= 0;
		}
		return ret;
	}

}
