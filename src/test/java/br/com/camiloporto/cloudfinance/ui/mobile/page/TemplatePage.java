package br.com.camiloporto.cloudfinance.ui.mobile.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public abstract class TemplatePage {
	
	public static final String RELATORIOS = "Relatórios";

	public static final String TRANSACOES = "Transações";

	public static final String CONTAS = "Contas";

	public static final String SISTEMA_DE_CONTAS = "Sistema de Contas";

	@FindBy(how = How.CSS, css="h2")
	private WebElement pageTitle;
	
	@FindBy(how = How.ID, id="errors")
	private WebElement errorList;
	
	private final String[] mainNavLinkTexts = new String[] {
		SISTEMA_DE_CONTAS, CONTAS, TRANSACOES, RELATORIOS
	};
	
	@FindAll({
		@FindBy(how = How.CSS, css = "#nav li a")
	})
	private List<WebElement> mainNavLinks;
	
	protected WebDriver webDriver;
	
	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
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
		for (String navLinkText : mainNavLinkTexts) {
			Assert.assertNotNull(getNavLink(navLinkText), "'" + navLinkText + "' not found on Main Navigation Menu");
			
		}
	}

	private WebElement getNavLink(String navLinkText) {
		for (WebElement link : mainNavLinks) {
			if(link.getText().equalsIgnoreCase(navLinkText)){
				return link;
			}
		}
		return null;
	}

	public void clickMainNavigationLink(String linkText) {
		WebElement link = getNavLink(linkText);
		link.click();
	}

	public void assertMainNavigationMenuIsNotPresent() {
		Assert.assertTrue(mainNavLinks.isEmpty(), "Should not find any nav links on this page");
	}

}
