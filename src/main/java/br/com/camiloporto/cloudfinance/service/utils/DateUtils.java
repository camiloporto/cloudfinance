package br.com.camiloporto.cloudfinance.service.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static final Date LOWEST_DATE = lowestDate();
	public static final Date HIGHEST_DATE = highestDate();
	
	private static Date lowestDate() {
		Calendar min = Calendar.getInstance();
		min.set(Calendar.YEAR, min.getActualMinimum(Calendar.YEAR));
		min.set(Calendar.MONTH, min.getActualMinimum(Calendar.MONTH));
		min.set(Calendar.DAY_OF_MONTH, min.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return min.getTime();
	}
	
	private static Date highestDate() {
		Calendar max = Calendar.getInstance();
		max.set(Calendar.YEAR, max.getActualMaximum(Calendar.YEAR));
		max.set(Calendar.MONTH, max.getActualMaximum(Calendar.MONTH));
		max.set(Calendar.DAY_OF_MONTH, max.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return max.getTime();
	}

}
