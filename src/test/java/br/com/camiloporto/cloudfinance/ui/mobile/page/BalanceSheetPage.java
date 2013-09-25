package br.com.camiloporto.cloudfinance.ui.mobile.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class BalanceSheetPage extends TemplatePage {
	
	@FindBy(how = How.CSS, css = "#balanceForm input[name=balanceDate]")
	private WebElement balanceDate;
	
	@FindBy(how = How.CSS, css = "#balanceForm input[type=submit]")
	private WebElement submitButton;

	@Override
	protected String getPageTitle() {
		return "Balanço";
	}

	public BalanceSheetPage fillBalanceDate(String date) {
		balanceDate.sendKeys(date);
		return this;
	}

	public BalanceSheetPage submit() {
		submitButton.submit();
		return this;
	}

}
