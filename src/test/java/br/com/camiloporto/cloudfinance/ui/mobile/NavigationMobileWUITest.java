package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TemplatePage;

public class NavigationMobileWUITest extends AbstractWUITest {
	
	@Test
	public void shouldLandInRootAccountPageAfterLogin() {
		loginExistentUser();
		TemplatePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.assertIsOnPage();
		rootAccountPage.assertMainNavigationMenuIsPresent();
	}
}
