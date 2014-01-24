package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import br.com.camiloporto.cloudfinance.model.Account;

@RooJavaBean
@RooToString
public class BalanceSheetNode {

	private Account account;
	private BigDecimal balance;
	private List<BalanceSheetNode> children = new ArrayList<BalanceSheetNode>();

	public BalanceSheetNode(Account account, BigDecimal balance) {
		this.account = account;
		this.balance = balance;
	}

	public BalanceSheetNode(Account account) {
		this.account = account;
	}
	
	protected void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
