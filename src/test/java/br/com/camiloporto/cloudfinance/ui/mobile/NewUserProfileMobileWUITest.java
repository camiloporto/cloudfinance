package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

public class NewUserProfileMobileWUITest extends AbstractWUITest {
	
	@Test
	public void shouldCreateNewUserProfile() {
		MobileHomePage mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.clickNewUserProfileLink();
		MobileNewUserPage newUserPage = PageFactory.initElements(driver, MobileNewUserPage.class);
		newUserPage
			.fillNewUserForm("user@gmail.com", "s3cret", "s3cret")
			.submit();
		
		MobileStatusPage statusPage = PageFactory.initElements(driver, MobileStatusPage.class);
		statusPage.assertPageTitleEquals("Cadastro de Usuario");
		statusPage.assertSuccess();
	}
	
	@Test
	public void shouldReportErrorsWhenFailCreatingNewUserProfile() {
		MobileHomePage mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.clickNewUserProfileLink();
		MobileNewUserPage newUserPage = PageFactory.initElements(driver, MobileNewUserPage.class);
		newUserPage
			.fillNewUserForm("", "", "") //no input informed
			.submit();
		
		MobileStatusPage statusPage = PageFactory.initElements(driver, MobileStatusPage.class);
		statusPage.assertPageTitleEquals("Cadastro de Usuario");
		statusPage.assertFail();
		statusPage.assertHasErrorMessages();
	}
}
