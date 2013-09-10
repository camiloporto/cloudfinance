package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;

public class AccountStatementMobileWUITest extends AbstractWUITest {

	@Test(enabled=false)
	public void shouldGenerateAccountStatementInGivenPeriodForAGivenAccount() {
		loginExistentUser();
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		goToPath("/report/statement");
	}
}
