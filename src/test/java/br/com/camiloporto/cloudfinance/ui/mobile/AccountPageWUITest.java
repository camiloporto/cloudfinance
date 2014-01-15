package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.helpers.DataInsertionHelper;
import br.com.camiloporto.cloudfinance.ui.mobile.page.AccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.FormNewAccountPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.MobileHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;

public class AccountPageWUITest extends AbstractWUITest {
	
	
	@Test
	public void shouldListAccountsOfSelectedRootAccount() {
		final String userName = generateSampleUserLogin();
		final String password = generateSampleUserPass();
		createNewTestUser(userName, password);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(userName);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.checkAccountsArePresent(userName, "Ativos", "Passivos", "Receitas", "Despesas");
	}
	
	@Test
	public void shouldShowFormForAddNewAccount() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(NEWUSER_GMAIL_COM);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.clickOnAccountLink("Ativos");
		FormNewAccountPage newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		newAccountForm.assertPageTitle("Nova Conta");
		newAccountForm.assertParentAccountName("Ativos");
	}
	
	@Test
	public void shouldAddNewAccount() {
		
		final String userName = generateSampleUserLogin();
		final String password = generateSampleUserPass();
		createNewTestUser(userName, password);
		
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(userName);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.clickOnAccountLink("Ativos");
		FormNewAccountPage newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		
		final String newName = "Ações";
		final String newDescription = "Patrimonio em ações";
		newAccountForm.fillNewAccount(newName, newDescription).submitForm();
		
		accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.checkAccountsArePresent(newName);
	}
	
	@Test
	public void shouldShowErrorsIfAddAccountFails() {
//		MobileHomePage mhp = PageFactory.initElements(driver,
//				MobileHomePage.class);
//		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		final String userName = generateSampleUserLogin();
		final String password = generateSampleUserPass();
		createNewTestUser(userName, password);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(userName);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.clickOnAccountLink("Ativos");
		FormNewAccountPage newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		
		final String newInvalidName = "";
		final String newDescription = "Patrimonio em ações";
		newAccountForm.fillNewAccount(newInvalidName, newDescription).submitForm();
		
		newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		newAccountForm.assertHasErrors();
	}
	
	@Test
	public void shouldAddAccountAfterErrorsOn1stAttemptS() {
//		MobileHomePage mhp = PageFactory.initElements(driver,
//				MobileHomePage.class);
//		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		final String userName = generateSampleUserLogin();
		final String password = generateSampleUserPass();
		createNewTestUser(userName, password);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver,
				RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(userName);
		AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.clickOnAccountLink("Ativos");
		FormNewAccountPage newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		
		final String newInvalidName = "";
		final String newDescription = "Patrimonio em ações";
		newAccountForm.fillNewAccount(newInvalidName, newDescription).submitForm();
		//error occurred on first attempt.
		
		newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		newAccountForm.assertHasErrors();
		
		newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
		
		final String newName = "Imoveis";
		newAccountForm.fillNewAccount(newName, newDescription).submitForm();
		
		accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
		accountHomePage.checkAccountsArePresent(newName);
	}
}
