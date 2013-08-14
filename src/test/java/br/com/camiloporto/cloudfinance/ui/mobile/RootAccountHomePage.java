package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class RootAccountHomePage {
	
	@FindBy(how = How.ID, id="rootAccountList")
	private WebElement rootAccountList;
	
	@FindBy(how=How.CSS, css="#rootAccountList li")
	private List<WebElement> rootAccountItems;

	public RootAccountHomePage checkRootAccountsArePresent(String... rootAccountNames) {
		for (String rootAccountName : rootAccountNames) {
			Assert.assertTrue(rootAccountisListed(rootAccountName), "expected root account not listed");
		}
		return this;
	}

	private Boolean rootAccountisListed(String rootAccountName) {
		for (WebElement rootAccountItem : rootAccountItems) {
			if(rootAccountItem.getText().contains(rootAccountName)){
				return true;
			}
		}
		return false;
	}

}
