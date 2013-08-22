package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.model.Account;

public class AccountPageWUITest {
	
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
	public void shouldListAccountsOfSelectedRootAccount() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.checkAccountsArePresent(NEWUSER_GMAIL_COM, "Ativos", "Passivos", "Receitas", "Despesas");
	}
}
