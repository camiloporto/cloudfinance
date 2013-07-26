package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class MobileStatusPage {
	
	private String pageName = "Status";
	
	@FindBy(how = How.ID, id="pageTitle")
	private WebElement pageTitle;

	public void assertSuccess() {
		Assert.fail("Implementar logica");
	}

	public void assertIsOnPage() {
		Assert.assertEquals(pageTitle.getText(), pageName);
	}

}
