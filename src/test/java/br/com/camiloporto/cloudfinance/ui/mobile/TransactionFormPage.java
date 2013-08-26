package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class TransactionFormPage extends TemplatePage {
	
	@FindBy(how=How.CSS, css="#formNewTransaction *[name=originAccountId]")
	private WebElement originAccountElement;
	
	@FindBy(how=How.CSS, css="#formNewTransaction *[name=destAccountId]")
	private WebElement destAccountElement;
	
	@FindBy(how=How.CSS, css="#formNewTransaction input[name=date]")
	private WebElement dateElement;
	
	@FindBy(how=How.CSS, css="#formNewTransaction input[name=amount]")
	private WebElement amountElement;
	
	@FindBy(how=How.CSS, css="#formNewTransaction input[name=description]")
	private WebElement descriptionElement;
	
	@FindBy(how=How.CSS, css="#formNewTransaction input[type=submit]")
	private WebElement submitBtn;

	public void assertFormElementsArePresent() {
		Assert.assertTrue(originAccountElement.isDisplayed(), "origin account field not present");
		Assert.assertTrue(destAccountElement.isDisplayed(), "dest account field not present");
		Assert.assertTrue(dateElement.isDisplayed(), "date field not present");
		Assert.assertTrue(amountElement.isDisplayed(), "amount field not present");
		Assert.assertTrue(descriptionElement.isDisplayed(), "description field not present");
		Assert.assertTrue(submitBtn.isDisplayed(), "submit button not present");
	}

}
