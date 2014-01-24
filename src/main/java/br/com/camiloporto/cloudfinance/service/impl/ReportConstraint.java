package br.com.camiloporto.cloudfinance.service.impl;

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
public class ReportConstraint {
	
	public interface ACCOUNT_STATEMENT {}
	public interface BALANCE_SHEET {}

	@NotNull(message = "{br.com.camiloporto.cloudfinance.user.LOGGED_USER_REQUIRED}",
			groups={ACCOUNT_STATEMENT.class, BALANCE_SHEET.class})
	private Profile profile;
	
	@NotNull(message = "{br.com.camiloporto.cloudfinance.account.ROOT_ACCOUNT_REQUIRED}",
			groups = {BALANCE_SHEET.class})
	private Long rootAccountId;
	
	@NotNull(message = "{br.com.camiloporto.cloudfinance.report.statement.ACCOUNT_REQUIRED}",
			groups = {ACCOUNT_STATEMENT.class})
	private Long accountId;
	
	private Date begin;
	private Date end;
	
	@NotNull(message = "{br.com.camiloporto.cloudfinance.report.balancesheet.DATE_REQUIRED}",
			groups = {BALANCE_SHEET.class})
	private Date balanceSheetDate;
	
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
