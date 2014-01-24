package br.com.camiloporto.cloudfinance.i18n;

import java.util.ResourceBundle;

public class ValidationMessages {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("ValidationMessages");
	
	public static final String USER_ID_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED");
	
	public static final String USER_PASS_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED");
	
	public static final String USER_ID_ALREADY_EXIST = BUNDLE.getString("br.com.camiloporto.cloudfinance.profile.USER_ID_ALREADY_EXIST");
	
	public static final String ORIGIN_ACCOUNT_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.transaction.ORIGIN_ACCOUNT_REQUIRED");
	
	public static final String TRANSACTION_ID_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.transaction.ID_REQUIRED");
	
	public static final String BEGIN_DATE_GREATER_THAN_END_DATE = BUNDLE.getString("br.com.camiloporto.cloudfinance.transaction.BEGIN_DATE_GREATER_THAN_END_DATE");
	
	public static final String ACCOUNT_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.report.statement.ACCOUNT_REQUIRED");
	
	public static final String PARENT_ACCOUNT_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.account.PARENT_ACCOUNT_REQUIRED");

	
}
