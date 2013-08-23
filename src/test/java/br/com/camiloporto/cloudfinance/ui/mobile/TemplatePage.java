package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class TemplatePage {
	
	@FindBy(how = How.CSS, css="h2")
	private WebElement pageTitle;
	
	public void assertPageTitle(String expectedPageTitle) {
		Assert.assertEquals(pageTitle.getText(), expectedPageTitle, "page title did not match");
	}

	

}
