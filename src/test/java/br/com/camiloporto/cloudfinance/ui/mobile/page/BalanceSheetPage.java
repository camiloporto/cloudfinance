package br.com.camiloporto.cloudfinance.ui.mobile.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class BalanceSheetPage extends TemplatePage {
	
	@FindBy(how = How.CSS, css = "#balanceForm input[name=balanceDate]")
	private WebElement balanceDate;
	
	@FindBy(how = How.CSS, css = "#balanceForm input[type=submit]")
	private WebElement submitButton;
	
	@FindAll ({
		@FindBy(how = How.CSS, css = "#balanceSheetDiv li")
	})
	private List<WebElement> balanceSheetEntryEl;
	
	private List<BalanceSheetEntryUI> expectedBalanceEntries = new ArrayList<BalanceSheetPage.BalanceSheetEntryUI>();
	
	@Override
	protected String getPageTitle() {
		return "Balanço";
	}

	public BalanceSheetPage fillBalanceDate(String date) {
		balanceDate.clear();
		balanceDate.sendKeys(date);
		return this;
	}

	public BalanceSheetPage submit() {
		submitButton.submit();
		return this;
	}

	public BalanceSheetPage checkBalanceEntries() {
		this.expectedBalanceEntries.clear();
		return this;
	}

	public BalanceSheetPage balanceEntry(String account, String expectedBalance) {
		BalanceSheetEntryUI entry = new BalanceSheetEntryUI(account, expectedBalance);
		this.expectedBalanceEntries.add(entry);
		return this;
	}

	public void arePresent() {
		for (BalanceSheetEntryUI expectedEntry : this.expectedBalanceEntries) {
			assertBalanceEntryIsPresent(expectedEntry);
		}
	}
	
	private void assertBalanceEntryIsPresent(BalanceSheetEntryUI expectedEntry) {
		boolean found = false;
		for (WebElement balanceEntryEl : this.balanceSheetEntryEl) {
			List<WebElement> values = balanceEntryEl.findElements(By.tagName("span"));
			WebElement account = values.get(0);
			WebElement balance = values.get(1);
			if(account.getText().contains(expectedEntry.getAccount())
					&& balance.getText().contains(expectedEntry.getBalance())) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "expected balance entry not found: " + expectedEntry);
	}

	static class BalanceSheetEntryUI {
		private String account;
		private String balance;
		
		public BalanceSheetEntryUI(String account, String balance) {
			super();
			this.account = account;
			this.balance = balance;
		}

		public BalanceSheetEntryUI() {
		}

		public String getAccount() {
			return account;
		}

		public String getBalance() {
			return balance;
		}
		
		@Override
		public String toString() {
			return "[" + account + " : " + balance + "]";
		}
	}

	public void assertBalanceDateEquals(String expectedBalanceDate) {
		String actualDate = balanceDate.getAttribute("value");
		Assert.assertEquals(actualDate, expectedBalanceDate, "expected balance date did not match");
	}

}
