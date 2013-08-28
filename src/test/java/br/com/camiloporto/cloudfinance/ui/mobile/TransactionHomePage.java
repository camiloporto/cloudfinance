package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;



public class TransactionHomePage extends TemplatePage {
	
	@FindAll({
		@FindBy(how=How.CSS, css = "ul li")
	})
	private List<WebElement> transactionList;
	
	@FindBy(how = How.CSS, css = "#filterForm input[name=begin]")
	private WebElement beginDateField;
	
	@FindBy(how = How.CSS, css = "#filterForm input[name=end]")
	private WebElement endDateField;
	
	@FindBy(how = How.CSS, css = "#filterForm input[type=submit]")
	private WebElement filterSubmit;

	public void assertTransactionsIsPresent(String originAccount, String destAccount,
			String date, String amount, String desc) {
		boolean found = false;
		for (WebElement transactionLi : transactionList) {
			if(isTransaction(transactionLi, date, originAccount, destAccount, amount, desc)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, String.format("expected transaction not found: %1$s %2$s %3$s %4$s %5$s", date, originAccount, destAccount, amount, desc));
	}

	private boolean isTransaction(WebElement transactionLi, String date,
			String originAccount, String destAccount, String amount, String desc) {
		String allText = transactionLi.getText();
		boolean isTransaction = true;
		isTransaction &= allText.contains(date);
		isTransaction &= allText.contains(originAccount);
		isTransaction &= allText.contains(destAccount);
		isTransaction &= allText.contains(amount);
		isTransaction &= allText.contains(desc);
		return isTransaction;
	}

	public TransactionHomePage fillTransactionDateFilter(String beginDate, String endDate) {
		beginDateField.sendKeys(beginDate);
		endDateField.sendKeys(endDate);
		return this;
	}

	public void submitDateFilter() {
		filterSubmit.submit();
		
	}

	public void assertTransactionsIsNotPresent(String originAccount, String destAccount,
			String date, String amount, String desc) {
		boolean found = false;
		for (WebElement transactionLi : transactionList) {
			if(isTransaction(transactionLi, date, originAccount, destAccount, amount, desc)) {
				found = true;
				break;
			}
		}
		Assert.assertFalse(found, String.format("expected transaction should not be found: %1$s %2$s %3$s %4$s %5$s", date, originAccount, destAccount, amount, desc));
	}
	
	private WebElement findTransactionLi(String originAccount, String destAccount,
			String date, String amount, String desc) {
		for (WebElement transactionLi : transactionList) {
			if(isTransaction(transactionLi, date, originAccount, destAccount, amount, desc)) {
				return transactionLi;
			}
		}
		return null;
	}

	public void showDetailOfTransaction(String originAccount, String destAccount,
			String date, String amount, String desc) {
		WebElement transactionLi = findTransactionLi(originAccount, destAccount, date, amount, desc);
		WebElement detailLink = transactionLi.findElement(By.cssSelector("a"));
		detailLink.click();
	}

}
