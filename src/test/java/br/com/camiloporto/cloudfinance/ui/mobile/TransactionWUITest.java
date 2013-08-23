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
	public void shouldAddNewTransaction() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		goToPath("/transaction");
		TransactionHomePage transactionPage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionPage.assertPageTitle("Transações");
		Assert.fail("COMPLETAR ESTE TESTE");
		//FIXME comppletar este teste
	}
}
