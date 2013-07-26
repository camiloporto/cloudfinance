package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewUserProfileMobileWUITest {
	
	private WebDriver driver;
	
	@BeforeClass
	public void startWebDriver() {
		driver = new FirefoxDriver();
		driver.get("http://localhost:8080/cloudfinance/mobile");
	}
	
	@AfterClass
	public void closeWebDriver() {
//		driver.close();
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
		statusPage.assertIsOnPage();
		
	}
}
