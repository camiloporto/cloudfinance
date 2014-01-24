package br.com.camiloporto.cloudfinance.web;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionFormBuilder {
	
	private TransactionForm form;
	
	public TransactionFormBuilder() {
		form = new TransactionForm();
	}

	public TransactionFormBuilder fromId(Long fromId) {
		form.setOriginAccountId(fromId);
		return this;
	}

	public TransactionFormBuilder destId(Long destId) {
		form.setDestAccountId(destId);
		return this;
	}

	public TransactionFormBuilder date(Date date) {
		form.setDate(date);
		return this;
	}

	public TransactionFormBuilder amount(BigDecimal amount) {
		form.setAmount(amount);
		return this;
	}

	public TransactionFormBuilder description(String desc) {
		form.setDescription(desc);
		return this;
	}

	public TransactionForm create() {
		return form;
	}

}
