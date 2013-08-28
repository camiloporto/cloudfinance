package br.com.camiloporto.cloudfinance.ui.mobile;

import java.util.List;

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

}
