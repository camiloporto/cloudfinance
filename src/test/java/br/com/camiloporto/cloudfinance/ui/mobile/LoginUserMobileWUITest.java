package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginUserMobileWUITest {

	private static final String NEWUSER_PASS = "s3cret";
	private static final String NEWUSER_GMAIL_COM = "newuser@gmail.com";
	private WebDriver driver;
	private boolean userCreated = false;

	@BeforeMethod
	public void startWebDriver() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("http://localhost:8080/cloudfinance/mobile");
	}

	@BeforeClass
	public void createNewUser() {
		if(!userCreated) {
			startWebDriver();
			MobileHomePage mhp = PageFactory.initElements(driver,
					MobileHomePage.class);
			mhp.clickNewUserProfileLink();
			MobileNewUserPage newUserPage = PageFactory.initElements(driver,
					MobileNewUserPage.class);
			newUserPage.fillNewUserForm(NEWUSER_GMAIL_COM, NEWUSER_PASS,
					NEWUSER_PASS).submit();
			closeWebDriver();
			userCreated = true;
		}
	}

	 @AfterMethod
	public void closeWebDriver() {
		driver.close();
	}

	@Test
	public void shouldLoginExistentUser() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.checkRootAccountsArePresent(NEWUSER_GMAIL_COM);

	}
	
	@Test
	public void shouldLogoffExistentLoggedUser() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.checkRootAccountsArePresent(NEWUSER_GMAIL_COM);
		
		rootAccountPage.logoff();
		String currentLocation = driver.getCurrentUrl();
		
		Assert.assertTrue(currentLocation.endsWith("/mobile"), "current location [" + currentLocation + "] did not match expected end path ");

	}

	@Test
	public void shouldStayOnLoginPageAndShowErrorMessageOnAuthenticationFailed() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, "wrong_pass");

		mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.assertLoginStatusMessageIs("Login failed");

	}
	
	@Test
	public void shouldGoDirectoToRootAccountPageIfHasALoggedUser() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		//successful login
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.checkRootAccountsArePresent(NEWUSER_GMAIL_COM);
		
		//goto home page again
		driver.get("http://localhost:8080/cloudfinance/mobile");
		
		//should redirect to root accounts page
		rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.checkRootAccountsArePresent(NEWUSER_GMAIL_COM);
		
	}

}
