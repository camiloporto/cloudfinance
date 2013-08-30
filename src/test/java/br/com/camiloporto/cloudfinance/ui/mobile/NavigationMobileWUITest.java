package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.ui.mobile.page.AccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TemplatePage;

public class NavigationMobileWUITest extends AbstractWUITest {
	
	@Test
	public void shouldLandInRootAccountPageAfterLogin() {
		loginExistentUser();
		TemplatePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.assertIsOnPage();
	}
	
	@Test
	public void shouldGoToAccountHomeAfterChooseAnAccountSystem() {
		loginExistentUser();
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.assertIsOnPage();
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.assertIsOnPage();
	}
	
	@Test
	public void accountSystemScreenShouldNotHaveMainNavigationMenu() {
		loginExistentUser();
		TemplatePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.assertIsOnPage();
		
		//nav menu should not be presente. user must choose an account system to work in
		rootAccountPage.assertMainNavigationMenuIsNotPresent();
	}
	
	@Test
	public void accountHomeScreenMustHaveMainNavigationMenu() {
		loginExistentUser();
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.assertIsOnPage();
		accountHomePage.assertMainNavigationMenuIsPresent();
	}
}
