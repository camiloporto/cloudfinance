package br.com.camiloporto.cloudfinance.ui.mobile.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class AccountStatementPage extends TemplatePage {
	
	@FindBy(how=How.CSS, css="#statementForm *[name=accountId]")
	private WebElement accountListElement;
	
	@FindBy(how = How.CSS, css = "#statementForm input[name=begin]")
	private WebElement beginDateField;
	
	@FindBy(how = How.CSS, css = "#statementForm input[name=end]")
	private WebElement endDateField;
	
	@FindBy(how=How.CSS, css="#statementForm input[type=submit]")
	private WebElement submitBtn;
	
	@FindBy(how = How.CSS, css = "#statementTable thead th:last-child")
	private WebElement previousBalanceEl;
	
	@FindBy(how = How.CSS, css = "#statementTable tfoot th:last-child")
	private WebElement finalBalanceEl;

	public AccountStatementPage selectAccount(String accountName) {
		Select accountSelect = new Select(accountListElement);
		accountSelect.selectByVisibleText(accountName);
		return this;
	}

	public AccountStatementPage requestStatement(String begin, String end) {
		beginDateField.sendKeys(begin);
		endDateField.sendKeys(end);
		submitBtn.submit();
		return this;
	}

	@Override
	protected String getPageTitle() {
		return "Extrato de Conta";
	}

	public AccountStatementPage assertPreviousBalanceIs(String prevBalance) {
		Assert.assertEquals(previousBalanceEl.getText(), prevBalance, "previous balance did not match expected value");
		return this;
	}

	public AccountStatementPage assertEndBalanceIs(String endBalance) {
		Assert.assertEquals(finalBalanceEl.getText(), endBalance, "final balance did not match expected value");
		return this;
	}

	public AccountStatementPage assertStatementEntryIsPresent(String date, String from, String to,
			String desc, String value) {
		// TODO Auto-generated method stub
		Assert.fail("implement this logic");
		return this;
	}

}
