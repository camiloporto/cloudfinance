package br.com.camiloporto.cloudfinance.service.impl;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean(settersByDefault=false)
@RooToString
public class BalanceSheet {

	private BalanceSheetNode assetBalanceSheetTree;
	private BalanceSheetNode liabilityBalanceSheetTree;

	public BalanceSheet(BalanceSheetNode assetBalanceSheetTree,
			BalanceSheetNode liabilityBalanceSheetTree) {
				this.assetBalanceSheetTree = assetBalanceSheetTree;
				this.liabilityBalanceSheetTree = liabilityBalanceSheetTree;
	}
	
}
