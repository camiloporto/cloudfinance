package br.com.camiloporto.cloudfinance.checkers;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.testng.Assert;

public class ExceptionChecker {
	
	private Throwable exception;

	public ExceptionChecker(Throwable e) {
		exception = e;
	}

	public void assertContainsMessageTemplate(String expected) {
		Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) exception).getConstraintViolations();
		boolean found = false;
		for (ConstraintViolation<?> constraintViolation : violations) {
			String actualTemplate = constraintViolation.getMessageTemplate();
			if(actualTemplate.equals(expected)){
				assertInterpolatedMessageExist(constraintViolation);
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "message template '" + expected + "' not found");
	}
	
	private void assertInterpolatedMessageExist(
			ConstraintViolation<?> constraintViolation) {
		Assert.assertNotEquals(constraintViolation.getMessage(), constraintViolation.getMessageTemplate(), "error message not i18nized: " + constraintViolation.getMessageTemplate());
	}

	public ExceptionChecker assertExpectedErrorCountIs(int expectedErrorCount) {
		Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) exception).getConstraintViolations();
		Assert.assertEquals(violations.size(), expectedErrorCount, "error count did not match");
		return this;
	}
}
