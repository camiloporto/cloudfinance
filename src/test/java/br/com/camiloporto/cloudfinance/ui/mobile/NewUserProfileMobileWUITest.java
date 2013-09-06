package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.ui.mobile.page.MobileHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.MobileNewUserPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.MobileStatusPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;

public class NewUserProfileMobileWUITest extends AbstractWUITest {
	
	@Test
	public void shouldCreateNewUserProfile() {
		MobileHomePage mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.clickNewUserProfileLink();
		MobileNewUserPage newUserPage = PageFactory.initElements(driver, MobileNewUserPage.class);
		String newUser = "user@gmail.com";
		newUserPage
			.fillNewUserForm(newUser, "s3cret", "s3cret")
			.submit();
		
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.assertIsOnPage();
		rootAccountPage.checkRootAccountsArePresent(newUser);
	}
	
	@Test
	public void shouldReportErrorsWhenFailCreatingNewUserProfile() {
		MobileHomePage mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.clickNewUserProfileLink();
		MobileNewUserPage newUserPage = PageFactory.initElements(driver, MobileNewUserPage.class);
		newUserPage
			.fillNewUserForm("", "", "") //no input informed
			.submit();
		
		newUserPage = PageFactory.initElements(driver, MobileNewUserPage.class);
		newUserPage.assertHasErrorMessages();
		
//		MobileStatusPage statusPage = PageFactory.initElements(driver, MobileStatusPage.class);
//		statusPage.assertPageTitleEquals("Cadastro de Usuario");
//		statusPage.assertFail();
//		statusPage.assertHasErrorMessages();
	}
}
