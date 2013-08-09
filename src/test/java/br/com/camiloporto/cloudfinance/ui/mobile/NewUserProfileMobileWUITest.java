package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NewUserProfileMobileWUITest {
	
	private WebDriver driver;
	
	@BeforeMethod
	public void startWebDriver() {
		driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("http://localhost:8080/cloudfinance/mobile");
	}
	
	@AfterMethod
	public void closeWebDriver() {
		driver.close();
	}
	
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
