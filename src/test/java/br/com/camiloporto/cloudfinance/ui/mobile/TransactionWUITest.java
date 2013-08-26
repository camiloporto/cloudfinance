package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TransactionWUITest extends AbstractWUITest {
	
//	@Override
//	protected WebDriver newWebDriver() {
//		return new FirefoxDriver();
//	}
	
	@Test
	public void shouldShowNewTransactionForm() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		goToPath("/transaction/newForm");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.assertPageTitle("Nova Transação");
		transactionFormPage.assertFormElementsArePresent();
		transactionFormPage.assertOriginAccountsAreListed("Passivos");
		transactionFormPage.assertDestAccountsAreListed("Despesas");
	}
}
