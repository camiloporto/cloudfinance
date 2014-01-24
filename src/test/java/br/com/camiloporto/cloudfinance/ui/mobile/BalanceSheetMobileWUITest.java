package br.com.camiloporto.cloudfinance.ui.mobile;

import java.io.FileNotFoundException;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.helpers.DataInsertionHelper;
import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.ui.mobile.page.AccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.BalanceSheetPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.FormNewAccountPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TransactionFormPage;

public class BalanceSheetMobileWUITest extends AbstractWUITest {
	
	private String sampleUserLogin;
	private String sampleUserPass;
	
	public BalanceSheetMobileWUITest() {
		sampleUserLogin = generateSampleUserLogin();
		sampleUserPass = generateSampleUserPass();
	}
	
//	@Override
//	protected WebDriver newWebDriver() {
//		return new FirefoxDriver();
//	}
	
	@BeforeClass
	public void insertSomeTransactions() throws FileNotFoundException, InterruptedException {
		startWebDriver();
		logoutExistentUser();
		createNewTestUser(sampleUserLogin, sampleUserPass);
		loginTestUser(sampleUserLogin, sampleUserPass);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(sampleUserLogin);
		List<String[]> accountList = new DataInsertionHelper().getDataAsArray(DataInsertionHelper.ACCOUNT_DATA);
		for (String[] line : accountList) {
			AccountHomePage accountHomePage = PageFactory.initElements(driver, AccountHomePage.class);
			accountHomePage.clickOnAccountLink(escapeRootAccounts(line[0].trim()));
			FormNewAccountPage newAccountForm = PageFactory.initElements(driver, FormNewAccountPage.class);
			newAccountForm.fillNewAccount(line[1].trim(), line[1] + " description").submitForm();
		}
		
		List<String[]> transactionList = new DataInsertionHelper().getDataAsArray(DataInsertionHelper.TRANSACTION_UI_FORM_DATA);
		
		for (String[] data : transactionList) {
			goToPath("/transaction/newForm?lang=pt_BR");
			TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
			transactionFormPage.fillNewTransaction(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4]).submit();
		}
		closeWebDriver();
	}
	
	private String escapeRootAccounts(String accountName) {
		String trimmed = accountName.trim();
		if(Account.ASSET_NAME.equals(trimmed)) {
			return "Ativos";
		}
		if(Account.LIABILITY_NAME.equals(trimmed)) {
			return "Passivos";
		}
		if(Account.INCOME_NAME.equals(trimmed)) {
			return "Receitas";
		}
		if(Account.OUTGOING_NAME.equals(trimmed)) {
			return "Despesas";
		}
		return trimmed;
	}
	
	@Test
	public void shouldGoToBalanceSheetHomePage() {
		loginTestUser(sampleUserLogin, sampleUserPass);
		goToPath("/report/balanceSheet");
		BalanceSheetPage balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.assertIsOnPage();
	}
	
	@Test
	public void shouldPersistUserFormInputsAmongAccountStatementsRequests() {
		loginTestUser(sampleUserLogin, sampleUserPass);
		goToPath("/report/balanceSheet");
		BalanceSheetPage balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.fillBalanceDate("20/09/2013");
		balanceSheetPage.submit();
		
		balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.assertBalanceDateEquals("20/09/2013");
	}

	@Test
	public void shouldDrawBalanceSheetOnScreen() {
		//given a logged user and some transactions saved...
		loginTestUser(sampleUserLogin, sampleUserPass);
		goToPath("/report/balanceSheet");
		BalanceSheetPage balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.fillBalanceDate("20/09/2013");
		balanceSheetPage.submit();
		
		balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.assertIsOnPage();
		balanceSheetPage.checkBalanceEntries()
			.balanceEntry("Fundo BB DI", "500,00")
			.balanceEntry("Cartao Credito", "-25,00")
			.balanceEntry("Ativos", "1.200,00")
			.balanceEntry("Passivos", "-25,00")
			.arePresent();
	}
	
	@Test
	public void shouldShowErrorsOnScreen() {
		//given a logged user and some transactions saved...
		loginTestUser(sampleUserLogin, sampleUserPass);
		goToPath("/report/balanceSheet");
		BalanceSheetPage balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.fillBalanceDate("invalidDate");
		balanceSheetPage.submit();
		
		balanceSheetPage = PageFactory.initElements(driver, BalanceSheetPage.class);
		balanceSheetPage.assertIsOnPage();
		balanceSheetPage.assertHasErrorMessages();
	}
}
