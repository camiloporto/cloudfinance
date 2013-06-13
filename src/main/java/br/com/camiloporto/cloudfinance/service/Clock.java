package br.com.camiloporto.cloudfinance.service;

import java.util.Calendar;

public class Clock {
	
	public Calendar today() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.HOUR, 0);
		
		return today;
	}

}
