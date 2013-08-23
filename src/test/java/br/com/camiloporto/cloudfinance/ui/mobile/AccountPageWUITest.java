package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

public class AccountPageWUITest extends AbstractWUITest {
	
	@Override
	protected WebDriver newWebDriver() {
		return new FirefoxDriver();
	}
	
	@Test
	public void shouldListAccountsOfSelectedRootAccount() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.checkAccountsArePresent(NEWUSER_GMAIL_COM, "Ativos", "Passivos", "Receitas", "Despesas");
	}
	
	@Test
	public void shouldShowFormForAddNewAccount() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.clickOnAccountLink("Ativos");
		FormNewAccountPage newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		newAccountForm.assertPageTitle("Nova Conta");
		newAccountForm.assertParentAccountName("Ativos");
	}
}
