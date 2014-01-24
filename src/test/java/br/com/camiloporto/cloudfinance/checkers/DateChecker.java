package br.com.camiloporto.cloudfinance.checkers;

import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;

public class DateChecker {
	
	
	public void assertDayMonthYearIsEquals(Date actual, Date expected, String msg) {
		Calendar actualCal = createFromDate(actual);
		Calendar expectedCal = createFromDate(expected);
		Assert.assertEquals(actualCal.get(Calendar.YEAR), expectedCal.get(Calendar.YEAR), msg + " - YEAR DID NOT MATCH");
		Assert.assertEquals(actualCal.get(Calendar.MONTH), expectedCal.get(Calendar.MONTH), msg + " - MONTH DID NOT MATCH");
		Assert.assertEquals(actualCal.get(Calendar.DAY_OF_MONTH), expectedCal.get(Calendar.DAY_OF_MONTH), msg + " - DAY DID NOT MATCH");
	}

	private Calendar createFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}
	
	public boolean isDayMonthYearEquals(Date actual, Date expected) {
		Calendar actualCal = createFromDate(actual);
		Calendar expectedCal = createFromDate(expected);
		return actualCal.get(Calendar.YEAR) == expectedCal.get(Calendar.YEAR) &&
				actualCal.get(Calendar.MONTH) == expectedCal.get(Calendar.MONTH) &&
				actualCal.get(Calendar.DAY_OF_MONTH) == expectedCal.get(Calendar.DAY_OF_MONTH);
	}

}
