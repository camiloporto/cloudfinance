package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.com.camiloporto.cloudfinance.ui.mobile.page.AccountStatementPage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.RootAccountHomePage;
import br.com.camiloporto.cloudfinance.ui.mobile.page.TransactionFormPage;

public class AccountStatementMobileWUITest extends AbstractWUITest {
	
	private final String TEST_USER_LOGIN = "reportUser@email.com";
	private final String TEST_USER_PASS = "report";
	private final String[][] transactionData = new String[][] {
			{"Ativos", "Despesas", "28/08/2013", "500,00", "Aluguel"},
			{"Receitas", "Ativos", "02/09/2013", "850,00", "Recebimento de salario"},
		};
	
	@BeforeClass
	public void insertSomeTransactions() {
		startWebDriver();
		logoutExistentUser();
		createNewTestUser(TEST_USER_LOGIN, TEST_USER_PASS);
		loginTestUser(TEST_USER_LOGIN, TEST_USER_PASS);
		RootAccountHomePage rootAccountPage = PageFactory.initElements(driver, RootAccountHomePage.class);
		rootAccountPage.selectRootAccount(TEST_USER_LOGIN);
		
		for (String[] data : transactionData) {
			goToPath("/transaction/newForm?lang=pt_BR");
			TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
			transactionFormPage.fillNewTransaction(data[0], data[1], data[2], data[3], data[4]).submit();
		}
		closeWebDriver();
	}

	@Test
	public void shouldGenerateAccountStatementInGivenPeriodForAGivenAccount() {
		//given a logged user and some transactions saved...
		loginTestUser(TEST_USER_LOGIN, TEST_USER_PASS);
		goToPath("/report/statement");
		AccountStatementPage statementPage = PageFactory.initElements(driver, AccountStatementPage.class);
		statementPage
			.selectAccount("Ativos")
			.requestStatement("01/09/2013", "30/09/2013");
		
		statementPage = PageFactory.initElements(driver, AccountStatementPage.class);
		statementPage.assertIsOnPage();
		statementPage
			.assertPreviousBalanceIs("-500,00")
			.assertEndBalanceIs("350,00")
			.assertStatementEntryIsPresent("02/09/2013", "Receitas", "Ativos", "Recebimento de salario", "850,00");
	}
}
