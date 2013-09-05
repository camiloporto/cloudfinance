package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import br.com.camiloporto.cloudfinance.ui.mobile.page.MobileHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.MobileNewUserPage;

public class AbstractWUITest {
	
	public static final String NEWUSER_PASS = "s3cret";
	public static final String NEWUSER_GMAIL_COM = "newuser@gmail.com";
	
	protected WebDriver driver;
	private boolean userCreated = false;
	private String domainPath = "http://localhost:8080/cloudfinance";
	
	protected void goToPath(String path) {
		driver.get(domainPath + path);
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
	
	protected WebDriver newWebDriver() {
		return new HtmlUnitDriver();
	}
	
	@BeforeMethod
	public void startWebDriver() {
		driver = newWebDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		goToPath("/mobile");
	}
	
	@AfterMethod
	public void closeWebDriver() {
		driver.close();
	}

	public void loginExistentUser() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
	}

}
