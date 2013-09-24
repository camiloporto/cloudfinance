package br.com.camiloporto.cloudfinance.service.impl;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean(settersByDefault=false)
@RooToString
public class BalanceSheet {

	private BalanceSheetNode assetBalanceSheetTree;
	private BalanceSheetNode liabilityBalanceSheetTree;
	
	private Date balanceDate;

	public BalanceSheet(BalanceSheetNode assetBalanceSheetTree,
			BalanceSheetNode liabilityBalanceSheetTree, Date date) {
				this.assetBalanceSheetTree = assetBalanceSheetTree;
				this.liabilityBalanceSheetTree = liabilityBalanceSheetTree;
				balanceDate = date;
	}
	
}
