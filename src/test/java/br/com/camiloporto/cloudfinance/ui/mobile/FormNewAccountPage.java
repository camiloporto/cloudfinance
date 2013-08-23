package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class FormNewAccountPage extends TemplatePage {
	
	@FindBy(how = How.CSS, css="h3")
	private WebElement parentAccountElement;

	public void assertParentAccountName(String expectedAccountName) {
		Assert.assertEquals(parentAccountElement.getText(), expectedAccountName, "parent account name did not match");
	}

}
