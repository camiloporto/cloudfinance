package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class MobileStatusPage {
	
	private static final String OPERATION_SUCCESS_MESSAGE = "Operação realizada com sucesso";

	private static final String OPERATION_FAILED_MESSAGE = "Operação não realizada";

	@FindBy(how = How.ID, id="pageTitle")
	private WebElement pageTitle;
	
	@FindBy(how = How.ID, id="statusMessage")
	private WebElement statusMessage;
	
	@FindBy(how = How.ID, id="errors")
	private WebElement errorList;

	public void assertSuccess() {
		Assert.assertEquals(statusMessage.getText(), OPERATION_SUCCESS_MESSAGE);
	}

	public void assertPageTitleEquals(String pageName) {
		Assert.assertEquals(pageTitle.getText(), pageName);
	}

	public void assertFail() {
		Assert.assertEquals(statusMessage.getText(), OPERATION_FAILED_MESSAGE);
	}

	public void assertHasErrorMessages() {
		try {
			Assert.assertFalse(errorList.findElements(By.tagName("li")).isEmpty(), "no error messages found");
		} catch (NoSuchElementException e) {
			Assert.fail("no error messages found");
		}
	}

}
