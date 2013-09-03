package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.ui.mobile.page.AccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.FormNewAccountPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TemplatePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TransactionDetailPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TransactionFormPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TransactionHomePage;

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
	
	@Test(dataProvider = "navigationMenuVerificationData")
	public void navigationMenuShouldBePresent(String pageUrl, Class<?> pageObjectClass) {
		loginExistentUser();
		goToPath(pageUrl);
		
		TemplatePage transactionHomePage = (TemplatePage) PageFactory.initElements(driver, pageObjectClass);
		transactionHomePage.assertIsOnPage();
		transactionHomePage.assertMainNavigationMenuIsPresent();
		
	}
	
	@DataProvider(name = "navigationMenuVerificationData")
	public Iterator<Object[]> navigationMenuVerificationData() {
		
		List<Object[]> testData = new ArrayList<Object[]>();
		
		//transaction home page
		testData.add(new Object[] {"/transaction", TransactionHomePage.class});
		testData.add(new Object[] {"/transaction/newForm", TransactionFormPage.class});
		testData.add(new Object[] {"/transaction/1", TransactionDetailPage.class});
		testData.add(new Object[] {"/account/showForm/1", FormNewAccountPage.class});
		
		return testData.iterator();
	}
	
	@Test(dataProvider = "navigationlinksDestinationData")
	public void navigationLinkShouldGoTo(String linkName, Class<?> expectedLandPageObject) {
		loginExistentUser();
		final String somePageWithMainMenuPath = "/transaction";
		goToPath(somePageWithMainMenuPath);
		
		TemplatePage somePageWithMainMenu = (TemplatePage) PageFactory.initElements(driver, TransactionHomePage.class);
		somePageWithMainMenu.clickMainNavigationLink(linkName);
		
		TemplatePage landPage = (TemplatePage) PageFactory.initElements(driver, expectedLandPageObject);
		landPage.assertIsOnPage();
	}
	
	@DataProvider(name = "navigationlinksDestinationData")
	public Iterator<Object[]> navigationlinksDestinationData() {
		
		List<Object[]> testData = new ArrayList<Object[]>();
		
		//from SISTEMA_CONTAS should go to root accounts home page
		testData.add(new Object[] {TemplatePage.SISTEMA_DE_CONTAS, RootAccountHomePage.class});
		testData.add(new Object[] {TemplatePage.TRANSACOES, TransactionHomePage.class});
		
		return testData.iterator();
	}
}
