package br.com.camiloporto.cloudfinance.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class AccountNode {
	
	private Account account;
	private List<AccountNode> children = new ArrayList<AccountNode>();
	
	public AccountNode(Account account, List<AccountNode> children) {
		this.account = account;
		this.children = children;
	}

	public AccountNode(Account nodeRootAccount) {
		this(nodeRootAccount, new ArrayList<AccountNode>());
	}

}
