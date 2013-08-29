package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

public class TransactionWUITest extends AbstractWUITest {
	
	@Override
	protected WebDriver newWebDriver() {
		return new FirefoxDriver();
	}
	
	public void loginExistentUser() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
	}
	
	@Test
	public void shouldShowNewTransactionForm() {
		loginExistentUser();
		goToPath("/transaction/newForm");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.assertPageTitle("Nova Transação");
		transactionFormPage.assertFormElementsArePresent();
		transactionFormPage.assertOriginAccountsAreListed("Passivos");
		transactionFormPage.assertDestAccountsAreListed("Despesas");
	}


	
	@Test
	public void shouldAddNewTransaction() {
		loginExistentUser();
		//FIXME trabalhar melhor o locale. vide http://www.mkyong.com/spring-mvc/spring-mvc-internationalization-example/
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.fillNewTransaction("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS").submit();

		TransactionHomePage transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.assertTransactionsIsPresent("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS");
	}
	
	@Test
	public void shouldShowErrorsOnAddingNewTransaction() {
		loginExistentUser();
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		
		//try to submit empty form. all required field are empty
		transactionFormPage.submit();
		
		transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.assertHasErrorMessages();
	}
	
	@Test
	public void shouldListTransactionsWithinDatePeriodInformed() {
		loginExistentUser();
		
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.fillNewTransaction("Receitas", "Despesas", "25/08/2013", "149,90", "pagamento de INSS").submit();
		
		goToPath("/transaction/newForm?lang=pt_BR");
		transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.fillNewTransaction("Receitas", "Despesas", "28/08/2013", "319,09", "Feira de Supermercado").submit();
		
		TransactionHomePage transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.fillTransactionDateFilter("26/08/2013", "30/08/2013").submitDateFilter();
		
		transactionHomePage.assertTransactionsIsPresent("Receitas", "Despesas", "28/08/2013", "319,09", "Feira de Supermercado");
		transactionHomePage.assertTransactionsIsNotPresent("Receitas", "Despesas", "25/08/2013", "149,90", "pagamento de INSS");
	}
	
	@Test
	public void shouldShowTransactionDetail() {
		loginExistentUser();
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.fillNewTransaction("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS").submit();

		TransactionHomePage transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.showDetailOfTransaction("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS");
		
		TransactionDetailPage detailPage = PageFactory.initElements(driver, TransactionDetailPage.class);
		detailPage.assertTransactionsIs("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS");
	}
	
	@Test
	public void shouldDeleteTransaction() {
		loginExistentUser();
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.fillNewTransaction("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS").submit();

		TransactionHomePage transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.showDetailOfTransaction("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS");
		
		TransactionDetailPage detailPage = PageFactory.initElements(driver, TransactionDetailPage.class);
		detailPage.deleteTransaction();
		
		transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.fillTransactionDateFilter("20/08/2013", "30/08/2013").submitDateFilter();
		transactionHomePage.assertTransactionsIsNotPresent("Receitas", "Despesas", "25/08/2013", "149,90", "pagamento de INSS");
	}
	
	@Test
	public void shouldNavigateFromTransactionListToNewTransactionScreen() {
		loginExistentUser();
		goToPath("/transaction");
		TransactionHomePage transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.clickNewTransactionNavigationLink();
		
		TemplatePage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.assertIsOnPage();
		
	}
	
	
}
