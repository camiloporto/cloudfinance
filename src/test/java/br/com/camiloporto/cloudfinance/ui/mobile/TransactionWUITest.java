package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

public class TransactionWUITest extends AbstractWUITest {
	
//	@Override
//	protected WebDriver newWebDriver() {
//		return new FirefoxDriver();
//	}
	
	@Test
	public void shouldShowNewTransactionForm() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		goToPath("/transaction/newForm");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.assertPageTitle("Nova Transação");
		transactionFormPage.assertFormElementsArePresent();
		transactionFormPage.assertOriginAccountsAreListed("Passivos");
		transactionFormPage.assertDestAccountsAreListed("Despesas");
	}
	
	@Test
	public void shouldAddNewTransaction() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		//FIXME trabalhar melhor o locale. vide http://www.mkyong.com/spring-mvc/spring-mvc-internationalization-example/
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.fillNewTransaction("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS").submit();

		TransactionHomePage transactionHomePage = PageFactory.initElements(driver, TransactionHomePage.class);
		transactionHomePage.assertTransactionsIsPresent("Receitas", "Despesas", "28/08/2013", "149,90", "pagamento de INSS");
	}
	
	@Test
	public void shouldShowErrorsOnAddingNewTransaction() {
		MobileHomePage mhp = PageFactory.initElements(driver,
				MobileHomePage.class);
		mhp.login(NEWUSER_GMAIL_COM, NEWUSER_PASS);
		goToPath("/transaction/newForm?lang=pt_BR");
		TransactionFormPage transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		
		//try to submit empty form. all required field are empty
		transactionFormPage.submit();
		
		transactionFormPage = PageFactory.initElements(driver, TransactionFormPage.class);
		transactionFormPage.assertHasErrorMessages();
	}
	
	
}
