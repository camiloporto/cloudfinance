package br.com.camiloporto.cloudfinance.i18n;

import java.util.ResourceBundle;

public class ValidationMessages {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("ValidationMessages");
	
	public static final String USER_ID_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.profile.USER_ID_REQUIRED");
	
	public static final String USER_PASS_REQUIRED = BUNDLE.getString("br.com.camiloporto.cloudfinance.profile.USER_PASS_REQUIRED");
	
	public static final String USER_ID_ALREADY_EXIST = BUNDLE.getString("br.com.camiloporto.cloudfinance.profile.USER_ID_ALREADY_EXIST");
	
	
}
