package br.com.camiloporto.cloudfinance.ui.mobile.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public abstract class TemplatePage {
	
	@FindBy(how = How.CSS, css="h2")
	private WebElement pageTitle;
	
	@FindBy(how = How.ID, id="errors")
	private WebElement errorList;
	
	public void assertPageTitle(String expectedPageTitle) {
		Assert.assertEquals(pageTitle.getText(), expectedPageTitle, "page title did not match");
	}
	
	public void assertHasErrorMessages() {
		try {
			Assert.assertFalse(errorList.findElements(By.tagName("li")).isEmpty(), "no error messages found");
		} catch (NoSuchElementException e) {
			Assert.fail("no error messages found");
		}
	}

	public void assertIsOnPage() {
		assertPageTitle(getPageTitle());
	}

	protected abstract String getPageTitle();

	public void assertMainNavigationMenuIsPresent() {
		Assert.fail("IMPLEMENTAR LOGICA E CRIAR MENU");
	}

}
