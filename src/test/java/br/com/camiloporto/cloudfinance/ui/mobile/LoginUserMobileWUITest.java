package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginUserMobileWUITest {
	
private WebDriver driver;
	
	@BeforeMethod
	public void startWebDriver() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("http://localhost:8080/cloudfinance/mobile");
	}
	
	@AfterMethod
	public void closeWebDriver() {
//		driver.close();
	}
	
	@Test
	public void shouldLoginExistentUser() {
		MobileHomePage mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.clickNewUserProfileLink();
		MobileNewUserPage newUserPage = PageFactory.initElements(driver, MobileNewUserPage.class);
		newUserPage
			.fillNewUserForm("newuser@gmail.com", "s3cret", "s3cret")
			.submit();
		driver.get("http://localhost:8080/cloudfinance/mobile");
		mhp = PageFactory.initElements(driver, MobileHomePage.class);
		mhp.login("newuser@gmail.com", "s3cret");
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.checkRootAccountsArePresent("newuser@gmail.com");
		
		//FIXME verificar se informacoes esperadas sao desenhadas no HTML - lista de links contas raizes
		//FIXME refatorar testes, torna-los repetiveis, mesmo que usuario ja cadastrado.
		//FIXME tratar erro quando usuario cadastrado ja existe. apresentar mensagem de erro. tratar isso.
		
	}
}
