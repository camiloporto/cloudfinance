package br.com.camiloporto.cloudfinance.ui.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class TransactionDetailPage extends TemplatePage {

	@FindBy(how = How.CSS, css="#detail")
	private WebElement transactionDetail;
	
	@FindBy(how = How.CSS, css="#deleteForm input[type=submit]")
	private WebElement deleteButton;

	public void assertTransactionsIs(String originAccount, String destAccount,
			String date, String amount, String desc) {
		Assert.assertTrue(
				isTransaction(transactionDetail, date, originAccount, destAccount, amount, desc), 
				String.format("expected transaction not found: %1$s %2$s %3$s %4$s %5$s", date, originAccount, destAccount, amount, desc));
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

	public void deleteTransaction() {
		deleteButton.submit();
	}

}
