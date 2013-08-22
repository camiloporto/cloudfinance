package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class AccountHomePage {

	@FindBy(how = How.CSS, css="h2 + ul")
	private WebElement rootAccountNode;
	
	public void checkAccountsArePresent(String... accounts) {
		String alltext = rootAccountNode.getText();
		for (String account : accounts) {
			Assert.assertTrue(alltext.contains(account), account + " not find in " + alltext);
		}
	}

}
