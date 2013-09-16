package br.com.camiloporto.cloudfinance.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.camiloporto.cloudfinance.model.Account;
import br.com.camiloporto.cloudfinance.model.AccountNode;
import br.com.camiloporto.cloudfinance.model.AccountSystem;
import br.com.camiloporto.cloudfinance.model.Profile;
import br.com.camiloporto.cloudfinance.repository.AccountEntryRepository;
import br.com.camiloporto.cloudfinance.repository.AccountRepository;
import br.com.camiloporto.cloudfinance.repository.AccountSystemRepository;
import br.com.camiloporto.cloudfinance.service.AccountManager;
import br.com.camiloporto.cloudfinance.service.BalanceSheetManager;
import br.com.camiloporto.cloudfinance.service.utils.DateUtils;

@Service
public class BalanceSheetManagerImpl implements BalanceSheetManager {

	@Autowired
	private AccountEntryRepository accountEntryRepository;
	
	@Autowired
	private AccountSystemRepository accountSystemRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountManager accountManager;
	
	@Override
	public BalanceSheet getBalanceSheet(Profile profile, Long rootAccountId, Date date) {
		checkGetBalanceSheetEntries(profile, rootAccountId, date);
		AccountSystem accountSystem = accountSystemRepository.findByUserProfileAndRootAccountId(profile, rootAccountId);
		Account asset = accountRepository.findByParentAccountAndName(accountSystem.getRootAccount(), Account.ASSET_NAME);
		Account liability = accountRepository.findByParentAccountAndName(accountSystem.getRootAccount(), Account.LIABILITY_NAME);
		
		AccountNode assetTree = accountManager.getAccountBranch(profile, asset.getId());
		AccountNode liabilityTree = accountManager.getAccountBranch(profile, liability.getId());
		
		BalanceSheetNode assetBalanceSheetNodes = createBalanceSheetTree(assetTree, date);
		BalanceSheetNode liabilityBalanceSheetNodes = createBalanceSheetTree(liabilityTree, date);
		BalanceSheet balanceSheet = new BalanceSheet(assetBalanceSheetNodes, liabilityBalanceSheetNodes);
		return balanceSheet;
	}

	private void checkGetBalanceSheetEntries(Profile profile,
			Long rootAccountId, Date date) {
		ReportConstraint constraints = new ReportConstraint();
		constraints.setProfile(profile);
		constraints.setAccountId(rootAccountId);
		constraints.setBalanceSheetDate(date);
		
		new ConstraintValidator<ReportConstraint>()
			.validateForGroups(constraints,
				ReportConstraint.BALANCE_SHEET.class);
	}

	private BalanceSheetNode createBalanceSheetTree(
			AccountNode rootAccountNode, Date date) {
		BalanceSheetNode rootSheetNode = new BalanceSheetNode(rootAccountNode.getAccount());
		appendBalanceSheetTree(rootSheetNode, date);
		return rootSheetNode;
	}

	private void appendBalanceSheetTree(BalanceSheetNode sheetNode,
			Date date) {
		List<Account> childrenAccounts = accountRepository.findByParentAccount(sheetNode.getAccount());
		if(childrenAccounts.isEmpty()) {
			BigDecimal balance = accountEntryRepository.sumBetween(DateUtils.LOWEST_DATE, date, sheetNode.getAccount());
			sheetNode.setBalance(balance);
		} else {
			for (Account account : childrenAccounts) {
				BalanceSheetNode childNode = new BalanceSheetNode(account);
				sheetNode.getChildren().add(childNode);
				appendBalanceSheetTree(childNode, date);
			}
			sheetNode.setBalance(sumChildrenNodeBalance(sheetNode));
		}
	}

	private BigDecimal sumChildrenNodeBalance(BalanceSheetNode node) {
		BigDecimal result = new BigDecimal("0.00");
		for (BalanceSheetNode child : node.getChildren()) {
			result = result.add(child.getBalance());
		}
		return result.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal getAccountBalance(Profile profile, Account account, Date date) {
		BigDecimal balance = accountEntryRepository.sumBetween(DateUtils.LOWEST_DATE, date, account);
		return balance;
	}

}
