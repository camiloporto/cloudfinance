package br.com.camiloporto.cloudfinance.ui.mobile.page;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
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
	
	@FindBy(how = How.XPATH, xpath = "//table[@id='statementTable']/thead/tr/th[last()]")
	private WebElement previousBalanceEl;
	
	@FindBy(how = How.XPATH, xpath = "//table[@id='statementTable']/tfoot/tr/th[last()]")
	private WebElement finalBalanceEl;
	
	@FindAll({
		@FindBy(how = How.CSS, css = "#statementTable tbody tr")
	})
	private List<WebElement> statementListEl;

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
		boolean found = true;
		for (WebElement statementEl : statementListEl) {
			String text = statementEl.getText();
			found &= text.contains(date);
			found &= text.contains(desc);
			found &= text.contains(value);
			found &= (text.contains(from) || text.contains(to));
		}
		Assert.assertTrue(found, "expected transaction not found " + date +" ; " + from + " ; " + to + " ; " + desc + " ; " + value);
		return this;
	}

}
