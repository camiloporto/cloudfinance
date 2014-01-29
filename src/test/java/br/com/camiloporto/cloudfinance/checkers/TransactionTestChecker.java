package br.com.camiloporto.cloudfinance.checkers;

import java.util.List;

import org.testng.Assert;

import br.com.camiloporto.cloudfinance.model.AccountTransaction;

public class TransactionTestChecker {

	public void assertThatTransactionsArePresent(
			List<AccountTransaction> result, String... txDescriptions) {
		for (String txDesc : txDescriptions) {
			boolean found = false;
			for (AccountTransaction tx : result) {
				if(txDesc.equals(tx.getOrigin().getComment())) {
					found = true;
				}
			}
			Assert.assertTrue(found, "transaction '" + txDesc + "' not found in result");
		}
	}

	public void assertThatTransactionsAreInOrder(
			List<AccountTransaction> actualTransactionsOrder, String... expectedTransactionsOrder) {
		Assert.assertEquals(actualTransactionsOrder.size(), expectedTransactionsOrder.length, "transactions lists must be equals in size");
		int i = 0;
		for (String transaction : expectedTransactionsOrder) {
			AccountTransaction nextTransaction = actualTransactionsOrder.get(i);
			Assert.assertEquals(nextTransaction.getDestin().getComment(), transaction, "transaction [" + i +"] is out of order");
			i++;
		}
	}

}
