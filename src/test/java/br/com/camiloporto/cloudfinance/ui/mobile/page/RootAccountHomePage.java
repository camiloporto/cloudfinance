package br.com.camiloporto.cloudfinance.ui.mobile.page;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class RootAccountHomePage extends TemplatePage {
	
	@FindBy(how = How.ID, id="rootAccountList")
	private WebElement rootAccountList;
	
	@FindBy(how = How.ID, id="logoffBtn")
	private WebElement logoffBtn;
	
	@FindBy(how=How.CSS, css="#rootAccountList li a")
	private List<WebElement> rootAccountItems;

	public TemplatePage checkRootAccountsArePresent(String... rootAccountNames) {
		for (String rootAccountName : rootAccountNames) {
			Assert.assertTrue(rootAccountIsListed(rootAccountName), "expected root account not listed");
		}
		return this;
	}

	private Boolean rootAccountIsListed(String rootAccountName) {
		for (WebElement rootAccountItem : rootAccountItems) {
			if(rootAccountItem.getText().contains(rootAccountName)){
				return true;
			}
		}
		return false;
	}

	public void logoff() {
		logoffBtn.submit();
	}

	public void selectRootAccount(String rootAccountName) {
		WebElement rootAccountLink = findRootAccountLink(rootAccountName);
		rootAccountLink.click();
	}

	private WebElement findRootAccountLink(String rootAccountName) {
		for (WebElement rootAccountItem : rootAccountItems) {
			if(rootAccountItem.getText().contains(rootAccountName)){
				return rootAccountItem;
			}
		}
		return null;
	}

	@Override
	protected String getPageTitle() {
		return "Sistemas de Contas";
	}

}
